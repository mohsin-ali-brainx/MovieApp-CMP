# Compose State Management & Recomposition Optimization Guide

## Table of Contents
1. [Single UI State Data Class Pattern](#single-ui-state-data-class-pattern)
2. [Advantages](#advantages)
3. [Drawbacks & Recomposition Issues](#drawbacks--recomposition-issues)
4. [How Recomposition Acts](#how-recomposition-acts)
5. [Precautions to Avoid Unnecessary Recomposition](#precautions-to-avoid-unnecessary-recomposition)
6. [Better Alternatives](#better-alternatives)
7. [Best Practices & Recommendations](#best-practices--recommendations)

---

## Single UI State Data Class Pattern

### Example Pattern

```kotlin
data class MainHomeScreenUiState(
    val searchText: String = ExtConstants.StringConstants.EMPTY,
    val searchResponse: SearchMultiMovieDto? = null,
    val isLoading: Boolean = false
)
```

This pattern involves creating a single data class that holds all UI state for a screen.

---

## Advantages

### ✅ 1. **Centralized State Management**
- All screen state is in one place, making it easy to understand the complete state of the screen
- Single source of truth for UI state

### ✅ 2. **Type Safety**
- Data class provides compile-time type checking
- Prevents runtime errors from incorrect state access

### ✅ 3. **Immutability**
- Data classes are immutable by default (when using `val`)
- Easy to create new instances with `copy()`
- Thread-safe by design

### ✅ 4. **Easy Testing**
- Single object to verify in unit tests
- Can easily create test instances with different state combinations

### ✅ 5. **Clear Contract**
- Explicit definition of what state the screen needs
- Self-documenting code structure

---

## Drawbacks & Recomposition Issues

### ❌ 1. **Unnecessary Recompositions**

**Problem:** When any property changes, the entire state object changes, causing ALL composables reading from it to recompose.

```kotlin
// When searchText changes, ALL composables recompose
val state by uiState.collectAsStateWithLifecycle()

// These all recompose even if only searchText changed:
SearchTextField(text = state.searchText)  // ✅ Needs update
LoadingIndicator(visible = state.isLoading) // ❌ Unnecessary recomposition
SearchResults(data = state.searchResponse) // ❌ Unnecessary recomposition
```

**Impact:**
- Performance degradation
- Unnecessary UI updates
- Battery drain on mobile devices

### ❌ 2. **Performance Impact**

- **Large Objects:** Complex objects like `SearchMultiMovieDto?` trigger recompositions even when unchanged
- **Frequent Updates:** Text input fields that update on every keystroke cause cascading recompositions
- **No Granular Control:** Can't selectively update only specific parts of the UI

### ❌ 3. **Memory Overhead**

- Each state change creates a new object instance
- Large nested objects are copied even if unchanged
- Increased garbage collection pressure

### ❌ 4. **Scalability Issues**

As the screen grows more complex:
- More properties = more unnecessary recompositions
- Harder to optimize individual components
- Difficult to track which property caused recomposition

---

## How Recomposition Acts

### Understanding the Problem

```kotlin
// Initial state
MainHomeScreenUiState(
    searchText = "",
    searchResponse = null,
    isLoading = false
)

// User types "a" → New instance created
MainHomeScreenUiState(
    searchText = "a",  // Changed
    searchResponse = null,  // Same reference, but new instance
    isLoading = false  // Same value, but new instance
)

// Compose sees: "New state object = recompose everything"
```

### Recomposition Flow

```
State Change (searchText: "" → "a")
    ↓
New State Object Created
    ↓
collectAsStateWithLifecycle() detects change
    ↓
ALL Composable functions reading state recompose
    ↓
Even composables that don't use searchText recompose
```

### Visual Example

```kotlin
@Composable
fun SearchScreen(uiState: MainHomeScreenUiState) {
    // This recomposes when searchText changes ✅
    SearchTextField(text = uiState.searchText)
    
    // This ALSO recomposes when searchText changes ❌
    // Even though isLoading hasn't changed!
    LoadingIndicator(visible = uiState.isLoading)
    
    // This ALSO recomposes when searchText changes ❌
    // Even though searchResponse hasn't changed!
    SearchResults(data = uiState.searchResponse)
}
```

---

## Precautions to Avoid Unnecessary Recomposition

### 1. Use `derivedStateOf` for Computed Values

```kotlin
@Composable
fun SearchScreen(uiState: MainHomeScreenUiState) {
    // Only recomposes when searchResponse actually changes
    val hasResults = remember(uiState.searchResponse) {
        derivedStateOf { uiState.searchResponse != null }
    }
    
    // Use hasResults instead of checking uiState.searchResponse directly
    if (hasResults.value) {
        SearchResults(uiState.searchResponse)
    }
}
```

**Benefits:**
- Prevents recomposition when unrelated state changes
- Only recomposes when the specific dependency changes

### 2. Extract Individual Properties with `remember`

```kotlin
@Composable
fun SearchScreen(uiState: MainHomeScreenUiState) {
    // Extract and memoize individual properties
    val searchText = remember { derivedStateOf { uiState.searchText } }
    val isLoading = remember { derivedStateOf { uiState.isLoading } }
    val searchResponse = remember { derivedStateOf { uiState.searchResponse } }
    
    // Use individual values instead of entire state
    SearchTextField(text = searchText.value)
    LoadingIndicator(visible = isLoading.value)
    SearchResults(data = searchResponse.value)
}
```

**Note:** This still has limitations - the parent still recomposes when any property changes.

### 3. Use `key()` for List Items

```kotlin
// If searchResponse contains a list
uiState.searchResponse?.results?.forEach { item ->
    key(item.id) {  // Prevents unnecessary recomposition
        SearchResultItem(item)
    }
}
```

**Benefits:**
- Compose can track individual items
- Only changed items recompose
- Stable keys prevent unnecessary work

### 4. Memoize Expensive Composable Functions

```kotlin
@Composable
fun SearchResults(searchResponse: SearchMultiMovieDto?) {
    // Memoize expensive operations
    val processedData = remember(searchResponse) {
        searchResponse?.results?.map { 
            // Expensive transformation
            processSearchResult(it)
        }
    }
    
    LazyColumn {
        items(processedData ?: emptyList()) { item ->
            SearchResultItem(item)
        }
    }
}
```

### 5. Use `@Stable` Annotation (if using interface)

```kotlin
@Stable
interface MainHomeScreenUiState {
    val searchText: String
    val searchResponse: SearchMultiMovieDto?
    val isLoading: Boolean
}
```

**Benefits:**
- Tells Compose that the type is stable
- Can help with recomposition skipping
- Works best with interfaces, not data classes

### 6. Split Composable Functions

```kotlin
@Composable
fun SearchScreen(uiState: MainHomeScreenUiState) {
    // Split into smaller composables
    // Each only receives the data it needs
    SearchInputSection(searchText = uiState.searchText)
    LoadingSection(isLoading = uiState.isLoading)
    ResultsSection(searchResponse = uiState.searchResponse)
}

@Composable
private fun SearchInputSection(searchText: String) {
    // Only recomposes when searchText changes
    SearchTextField(text = searchText)
}

@Composable
private fun LoadingSection(isLoading: Boolean) {
    // Only recomposes when isLoading changes
    LoadingIndicator(visible = isLoading)
}

@Composable
private fun ResultsSection(searchResponse: SearchMultiMovieDto?) {
    // Only recomposes when searchResponse changes
    SearchResults(data = searchResponse)
}
```

**Note:** This still requires the parent to recompose, but child composables can skip recomposition if their parameters haven't changed.

---

## Better Alternatives

### Alternative 1: Separate State Flows ⭐ **Recommended**

```kotlin
class MainHomeViewModel : ViewModel() {
    // Separate flows for different concerns
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()
    
    private val _searchResponse = MutableStateFlow<SearchMultiMovieDto?>(null)
    val searchResponse: StateFlow<SearchMultiMovieDto?> = _searchResponse.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
}

@Composable
fun SearchScreen(viewModel: MainHomeViewModel) {
    // Only recomposes when searchText changes
    val searchText by viewModel.searchText.collectAsStateWithLifecycle()
    
    // Only recomposes when isLoading changes
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    
    // Only recomposes when searchResponse changes
    val searchResponse by viewModel.searchResponse.collectAsStateWithLifecycle()
    
    // Now each composable only recomposes when its specific state changes
    SearchInputSection(searchText = searchText)
    LoadingSection(isLoading = isLoading)
    ResultsSection(searchResponse = searchResponse)
}
```

**Advantages:**
- ✅ Granular recomposition control
- ✅ Only affected composables recompose
- ✅ Better performance
- ✅ Clear separation of concerns
- ✅ Easier to test individual state pieces

**Disadvantages:**
- ❌ More boilerplate code
- ❌ Multiple flows to manage
- ❌ Slightly more complex ViewModel

### Alternative 2: Grouped State Classes

```kotlin
// Group related state together
data class SearchUiState(
    val text: String = "",
    val response: SearchMultiMovieDto? = null
)

data class LoadingUiState(
    val isLoading: Boolean = false
)

// Main state combines groups
data class MainHomeScreenUiState(
    val search: SearchUiState = SearchUiState(),
    val loading: LoadingUiState = LoadingUiState()
)

// Usage
@Composable
fun SearchScreen(uiState: MainHomeScreenUiState) {
    val searchState = remember { derivedStateOf { uiState.search } }
    val loadingState = remember { derivedStateOf { uiState.loading } }
    
    SearchInputSection(searchText = searchState.value.text)
    LoadingSection(isLoading = loadingState.value.isLoading)
    ResultsSection(searchResponse = searchState.value.response)
}
```

**Advantages:**
- ✅ Logical grouping of related state
- ✅ Better organization
- ✅ Can still use single state object

**Disadvantages:**
- ❌ Still has recomposition issues if not careful
- ❌ Requires careful use of `remember` and `derivedStateOf`

### Alternative 3: Sealed Class for State (Best for Async Operations)

```kotlin
sealed class SearchState {
    data object Idle : SearchState()
    data object Loading : SearchState()
    data class Success(val data: SearchMultiMovieDto) : SearchState()
    data class Error(val message: String) : SearchState()
}

data class MainHomeScreenUiState(
    val searchText: String = "",
    val searchState: SearchState = SearchState.Idle
)

// Usage
@Composable
fun SearchScreen(uiState: MainHomeScreenUiState) {
    SearchInputSection(searchText = uiState.searchText)
    
    when (val state = uiState.searchState) {
        is SearchState.Idle -> { /* Show initial state */ }
        is SearchState.Loading -> LoadingIndicator()
        is SearchState.Success -> ResultsSection(data = state.data)
        is SearchState.Error -> ErrorMessage(message = state.message)
    }
}
```

**Advantages:**
- ✅ Type-safe state representation
- ✅ Clear state transitions
- ✅ Good for async operations (loading, success, error)
- ✅ Exhaustive when expressions

**Disadvantages:**
- ❌ Still recomposes when searchText changes
- ❌ May need to combine with separate flows for best results

### Alternative 4: Use `snapshotFlow` for Selective Observation

```kotlin
@Composable
fun SearchScreen(uiState: MainHomeScreenUiState) {
    // Only observe specific property
    val searchText by remember {
        snapshotFlow { uiState.searchText }
    }.collectAsStateWithLifecycle(initial = uiState.searchText)
    
    // This only recomposes when searchText changes
    SearchInputSection(searchText = searchText)
}
```

**Note:** This is more of a workaround and may not work well with StateFlow.

---

## Best Practices & Recommendations

### For Simple Screens (Few State Properties)

**Use:** Single UI State Data Class
```kotlin
data class SimpleScreenUiState(
    val text: String = "",
    val isEnabled: Boolean = false
)
```

**When to use:**
- Simple forms
- Settings screens
- Screens with 2-3 state properties
- No frequent updates

### For Complex Screens (Multiple State Properties, Frequent Updates)

**Use:** Separate State Flows ⭐
```kotlin
class ComplexScreenViewModel : ViewModel() {
    val textInput: StateFlow<String>
    val isLoading: StateFlow<Boolean>
    val data: StateFlow<Data?>
    // ... more flows
}
```

**When to use:**
- Search screens (frequent text input)
- Forms with many fields
- Screens with async operations
- Screens with complex state interactions

### For Async Operations

**Use:** Sealed Class + Separate Flows
```kotlin
sealed class DataState {
    data object Loading : DataState()
    data class Success(val data: Data) : DataState()
    data class Error(val message: String) : DataState()
}

class ViewModel : ViewModel() {
    val searchText: StateFlow<String>
    val dataState: StateFlow<DataState>
}
```

### Recommended Pattern for Your Use Case

Given your example with `searchText`, `searchResponse`, and `isLoading`:

```kotlin
class MainHomeViewModel : ViewModel() {
    // Fast-changing state (text input)
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()
    
    // Slow-changing state (API response)
    private val _searchState = MutableStateFlow<SearchState>(SearchState.Idle)
    val searchState: StateFlow<SearchState> = _searchState.asStateFlow()
    
    fun updateSearchText(text: String) {
        _searchText.value = text
    }
    
    fun performSearch(query: String) {
        viewModelScope.launch {
            _searchState.value = SearchState.Loading
            try {
                val result = searchRepository.search(query)
                _searchState.value = SearchState.Success(result)
            } catch (e: Exception) {
                _searchState.value = SearchState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class SearchState {
    data object Idle : SearchState()
    data object Loading : SearchState()
    data class Success(val data: SearchMultiMovieDto) : SearchState()
    data class Error(val message: String) : SearchState()
}

@Composable
fun MainHomeScreen(viewModel: MainHomeViewModel) {
    val searchText by viewModel.searchText.collectAsStateWithLifecycle()
    val searchState by viewModel.searchState.collectAsStateWithLifecycle()
    
    Column {
        // Only recomposes when searchText changes
        SearchTextField(
            text = searchText,
            onTextChange = viewModel::updateSearchText
        )
        
        // Only recomposes when searchState changes
        when (searchState) {
            is SearchState.Idle -> { /* Initial state */ }
            is SearchState.Loading -> LoadingIndicator()
            is SearchState.Success -> SearchResults(data = searchState.data)
            is SearchState.Error -> ErrorMessage(message = searchState.message)
        }
    }
}
```

### Performance Comparison

| Pattern | Recomposition Frequency | Performance | Complexity |
|---------|-------------------------|-------------|------------|
| Single Data Class | High (all properties) | ⭐⭐ | ⭐⭐⭐⭐⭐ |
| Separate Flows | Low (only changed) | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ |
| Grouped Classes | Medium | ⭐⭐⭐ | ⭐⭐⭐⭐ |
| Sealed Class | Medium-High | ⭐⭐⭐ | ⭐⭐⭐⭐ |

### Migration Strategy

If you're currently using a single data class and want to migrate:

1. **Identify frequently changing properties** (e.g., `searchText`)
2. **Extract to separate StateFlow**
3. **Keep stable properties in data class** (e.g., `isLoading`, `searchResponse`)
4. **Gradually migrate other properties as needed**
5. **Test recomposition with Layout Inspector**

### Tools for Debugging Recomposition

1. **Layout Inspector** - Visualize recompositions
2. **Recomposition Counter** - Add debug logging
3. **Compose Compiler Metrics** - Analyze recomposition patterns

```kotlin
// Debug recomposition
@Composable
fun SearchScreen(uiState: MainHomeScreenUiState) {
    val recompositionCount = remember { mutableIntStateOf(0) }
    recompositionCount.intValue++
    
    LaunchedEffect(Unit) {
        Log.d("Recomposition", "SearchScreen recomposed ${recompositionCount.intValue} times")
    }
    
    // Your composable code
}
```

---

## Summary

### Key Takeaways

1. **Single data class is fine for simple screens** with few, infrequently changing properties
2. **Separate StateFlows are better for complex screens** with frequent updates
3. **Always consider recomposition impact** when designing state
4. **Use `derivedStateOf` and `remember`** to minimize recompositions
5. **Profile your app** to identify actual performance bottlenecks

### Decision Matrix

```
Is the screen simple with < 3 properties?
├─ Yes → Use Single Data Class
└─ No → Does it have frequent updates (text input, animations)?
    ├─ Yes → Use Separate StateFlows ⭐
    └─ No → Use Single Data Class with optimizations
```

### Final Recommendation

For your `MainHomeScreenUiState` example with `searchText`, `searchResponse`, and `isLoading`:

**✅ Use Separate StateFlows** because:
- `searchText` changes frequently (every keystroke)
- `searchResponse` is a large object that shouldn't trigger recomposition on text changes
- `isLoading` is independent and should only recompose when loading state changes

This approach will provide the best performance and user experience.

---

## Additional Resources

- [Jetpack Compose State Documentation](https://developer.android.com/jetpack/compose/state)
- [Compose Performance Best Practices](https://developer.android.com/jetpack/compose/performance)
- [StateFlow vs LiveData](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow)
- [Compose Recomposition](https://developer.android.com/jetpack/compose/mental-model#recomposition)

---

*Last Updated: 2025*
*Project: CMP_TicketTribe*

