# State Handling & Recomposition Optimization Guide

## Table of Contents
1. [Single UiState Data Class Overview](#single-uistate-data-class-overview)
2. [Advantages](#advantages)
3. [Drawbacks](#drawbacks)
4. [How Recomposition Works](#how-recomposition-works)
5. [Avoiding Unnecessary Recompositions](#avoiding-unnecessary-recompositions)
6. [Best Practices](#best-practices)
7. [Precautions](#precautions)
8. [Alternative Approaches](#alternative-approaches)
9. [Recommended Implementation](#recommended-implementation)

---

## Single UiState Data Class Overview

A single UiState data class consolidates all UI-related state into one immutable data structure. This pattern is commonly used in Jetpack Compose applications for state management.

### Example Implementation

```kotlin
data class MainHomeScreenUiState(
    val searchText: String = ExtConstants.StringConstants.EMPTY,
    val searchResponse: SearchMultiMovieDto? = null,
    val isLoading: Boolean = false
)
```

---

## Advantages

### 1. **Centralized State Management**
- All UI state is in one place, making it easier to understand the complete state of a screen
- Single source of truth for UI state
- Easier to debug and inspect state changes

### 2. **Type Safety**
- Compile-time safety for state changes
- Prevents invalid state combinations
- IDE autocomplete support

### 3. **Immutability**
- Data classes with `copy()` support immutable updates
- Thread-safe by default
- Predictable state transitions

### 4. **Testability**
- Easy to create test states
- Simple to verify state changes
- Can test state transitions independently

### 5. **State Consistency**
- All properties update atomically
- No partial state updates
- Prevents UI inconsistencies

### 6. **Simpler API**
- One state object to pass around
- Cleaner function signatures
- Less parameter passing

---

## Drawbacks

### 1. **Unnecessary Recompositions**
- Any property change causes all consumers to recompose
- Components reading unchanged properties still recompose
- Can lead to performance issues in complex UIs

### 2. **Memory Overhead**
- Large state objects consume more memory
- Entire state object is kept in memory even if only one property is used
- Can be problematic with large data structures

### 3. **Tight Coupling**
- UI components become dependent on the entire state structure
- Changes to one property affect all consumers
- Harder to refactor individual components

### 4. **Performance Concerns**
- Frequent updates to one property trigger full recompositions
- Can cause UI jank in complex screens
- May require additional optimization techniques

### 5. **Scalability Issues**
- As state grows, maintenance becomes harder
- Large state classes are harder to understand
- Risk of state becoming a "god object"

---

## How Recomposition Works

### Current Implementation Flow

```kotlin
@Composable
fun MainHomeScreen(
    dataState: StateFlow<MainHomeScreenUiState>,
    ...
) {
    val state by dataState.collectAsStateWithLifecycle()
    MainContent(state, ...)
}

@Composable
private fun MainContent(
    dataState: MainHomeScreenUiState,
    ...
) {
    SearchBar(text = dataState.searchText, ...)
    LazyColumn(items = dataState.searchResponse?.result ?: emptyList()) { ... }
    if (dataState.isLoading) { CircularProgressIndicator() }
}
```

### Recomposition Behavior

When any property in `MainHomeScreenUiState` changes:

1. **StateFlow Emission**: `collectAsStateWithLifecycle()` emits a new state instance
2. **Top-Level Recomposition**: `MainHomeScreen` recomposes because it receives a new state
3. **Cascading Recomposition**: `MainContent` recomposes because it receives a new `dataState` parameter
4. **Child Recomposition**: All child composables that read from `dataState` may recompose

### Example Scenario

**When `searchText` changes:**
- ✅ `SearchBar` recomposes (needs `searchText`) - **Necessary**
- ❌ `LazyColumn` recomposes (only needs `searchResponse`) - **Unnecessary**
- ❌ `CircularProgressIndicator` recomposes (only needs `isLoading`) - **Unnecessary**

**When `isLoading` changes:**
- ❌ `SearchBar` recomposes (only needs `searchText`) - **Unnecessary**
- ❌ `LazyColumn` recomposes (only needs `searchResponse`) - **Unnecessary**
- ✅ `CircularProgressIndicator` recomposes (needs `isLoading`) - **Necessary**

---

## Avoiding Unnecessary Recompositions

### 1. Destructure State Properties

**Before:**
```kotlin
@Composable
private fun MainContent(
    dataState: MainHomeScreenUiState,
    onIntent: (MainHomeScreenUiIntents) -> Unit
) {
    SearchBar(text = dataState.searchText, ...)
    LazyColumn(items = dataState.searchResponse?.result ?: emptyList()) { ... }
    if (dataState.isLoading) { CircularProgressIndicator() }
}
```

**After:**
```kotlin
@Composable
private fun MainContent(
    dataState: MainHomeScreenUiState,
    onIntent: (MainHomeScreenUiIntents) -> Unit
) {
    // Destructure to allow Compose to track individual properties
    val searchText = dataState.searchText
    val searchResponse = dataState.searchResponse
    val isLoading = dataState.isLoading
    
    // Now only components using changed properties will recompose
    SearchBar(text = searchText, ...)
    LazyColumn(items = searchResponse?.result ?: emptyList()) { ... }
    if (isLoading) { CircularProgressIndicator() }
}
```

**Why it works:** Compose can track individual property reads and only recompose composables that read changed properties.

---

### 2. Use `derivedStateOf` for Computed Values

```kotlin
@Composable
private fun MainContent(
    dataState: MainHomeScreenUiState,
    onIntent: (MainHomeScreenUiIntents) -> Unit
) {
    val searchResults = remember(dataState.searchResponse) {
        derivedStateOf { 
            dataState.searchResponse?.result ?: emptyList() 
        }
    }
    
    LazyColumn(items = searchResults.value) { ... }
}
```

**Why it works:** `derivedStateOf` only recomposes when its dependencies change, preventing unnecessary recompositions.

---

### 3. Extract Sub-Composables with Specific Parameters

**Before:**
```kotlin
@Composable
private fun MainContent(
    dataState: MainHomeScreenUiState,
    onIntent: (MainHomeScreenUiIntents) -> Unit
) {
    SearchBar(text = dataState.searchText, ...)
    LazyColumn(items = dataState.searchResponse?.result ?: emptyList()) { ... }
}
```

**After:**
```kotlin
@Composable
private fun MainContent(
    dataState: MainHomeScreenUiState,
    onIntent: (MainHomeScreenUiIntents) -> Unit
) {
    SearchSection(
        searchText = dataState.searchText,
        onSearchTextChange = { 
            onIntent(MainHomeScreenUiIntents.TextFieldsIntent.OnSearchTextUpdate(it)) 
        },
        onSearchClick = { 
            onIntent(MainHomeScreenUiIntents.ButtonIntents.OnSearchButtonIntent) 
        }
    )
    
    ResultsList(
        items = dataState.searchResponse?.result ?: emptyList(),
        onItemClick = { media ->
            onIntent(MainHomeScreenUiIntents.ListItemIntent.OnMovieItemClick(media))
        }
    )
}

@Composable
private fun SearchSection(
    searchText: String,  // Only receives what it needs
    onSearchTextChange: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    SearchBar(text = searchText, onValueChange = onSearchTextChange)
    PrimaryButton(onClick = onSearchClick) { ... }
}

@Composable
private fun ResultsList(
    items: List<MovieTypeDTO>,  // Only receives what it needs
    onItemClick: (Media) -> Unit
) {
    LazyColumn(items = items) { ... }
}
```

**Why it works:** Smaller composables with specific parameters only recompose when their specific parameters change.

---

### 4. Use `remember` for Stable References

```kotlin
@Composable
private fun MainContent(
    dataState: MainHomeScreenUiState,
    onIntent: (MainHomeScreenUiIntents) -> Unit
) {
    // Remember the list to prevent recreation on every recomposition
    val searchResults = remember(dataState.searchResponse) {
        dataState.searchResponse?.result ?: emptyList()
    }
    
    LazyColumn(items = searchResults) { ... }
}
```

**Why it works:** `remember` prevents object recreation unless dependencies change, reducing unnecessary work.

---

### 5. Implement Custom Equality for Data Classes

```kotlin
data class MainHomeScreenUiState(
    val searchText: String = ExtConstants.StringConstants.EMPTY,
    val searchResponse: SearchMultiMovieDto? = null,
    val isLoading: Boolean = false
) {
    // Custom equals to prevent unnecessary updates
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MainHomeScreenUiState) return false
        
        return searchText == other.searchText &&
               searchResponse == other.searchResponse &&
               isLoading == other.isLoading
    }
    
    override fun hashCode(): Int {
        var result = searchText.hashCode()
        result = 31 * result + (searchResponse?.hashCode() ?: 0)
        result = 31 * result + isLoading.hashCode()
        return result
    }
}
```

**Note:** This is usually not necessary as data classes already implement proper equality, but can be useful for complex nested objects.

---

## Best Practices

### 1. Keep State Classes Small

**Guideline:** Limit to 5-10 properties per state class.

**If state grows large, consider splitting:**
```kotlin
// Instead of one large state
data class MainHomeScreenUiState(
    val searchText: String,
    val searchResponse: SearchMultiMovieDto?,
    val isLoading: Boolean,
    val errorMessage: String?,
    val selectedFilter: Filter?,
    val sortOrder: SortOrder,
    val showFilters: Boolean,
    val paginationInfo: PaginationInfo?
)

// Split into logical groups
data class MainHomeScreenUiState(
    val search: SearchState = SearchState(),
    val results: ResultsState = ResultsState(),
    val ui: UiState = UiState()
) {
    data class SearchState(
        val text: String = "",
        val filter: Filter? = null,
        val sortOrder: SortOrder = SortOrder.DEFAULT
    )
    
    data class ResultsState(
        val data: SearchMultiMovieDto? = null,
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val paginationInfo: PaginationInfo? = null
    )
    
    data class UiState(
        val showFilters: Boolean = false
    )
}
```

---

### 2. Use Stable Types

**Prefer:**
- Primitives (`String`, `Int`, `Boolean`, etc.)
- Immutable collections (`List`, `Set`, `Map`)
- Data classes with stable properties

**Avoid:**
- Mutable collections (`MutableList`, `MutableSet`, etc.)
- Complex objects with mutable state
- Functions or lambdas in state

```kotlin
// ❌ Bad
data class UiState(val list: MutableList<Item>)

// ✅ Good
data class UiState(val list: List<Item>)
```

---

### 3. Separate UI State from Business Logic

```kotlin
// UI-specific state
data class MainHomeScreenUiState(
    val searchText: String,
    val isLoading: Boolean,
    val errorMessage: String?,
    val showFilters: Boolean
)

// Business data (separate)
data class SearchResults(
    val movies: List<Movie>,
    val metadata: Metadata
)
```

---

### 4. Use `@Stable` Annotation for Custom Types

```kotlin
@Stable
data class SearchMultiMovieDto(
    val result: List<MovieTypeDTO>,
    val metaData: Metadata?
)

@Stable
data class MovieTypeDTO(
    val mediaType: String,
    val mediaItems: List<MediaItem>
)
```

**Why:** Tells Compose that these types are stable and won't change identity unless their properties change.

---

### 5. Minimize State Updates

**Bad:**
```kotlin
// Multiple updates trigger multiple recompositions
_state.update { it.copy(searchText = newText) }
_state.update { it.copy(isLoading = true) }
_state.update { it.copy(errorMessage = null) }
```

**Good:**
```kotlin
// Single update triggers one recomposition
_state.update { 
    it.copy(
        searchText = newText,
        isLoading = true,
        errorMessage = null
    )
}
```

---

### 6. Use `key()` in LazyColumn for Stable Keys

```kotlin
itemsIndexed(
    items = dataState.searchResponse?.result ?: emptyList(),
    key = { index, item -> 
        // Use stable, unique identifier
        item.mediaType 
    }
) { index, item ->
    MovieCarousal(data = item, ...)
}
```

**Why:** Stable keys help Compose identify items efficiently and prevent unnecessary recompositions.

---

## Precautions

### 1. Avoid Mutable Objects in State

```kotlin
// ❌ Bad - Mutable state can cause issues
data class UiState(
    val list: MutableList<Item>,
    val map: MutableMap<String, Value>
)

// ✅ Good - Immutable state
data class UiState(
    val list: List<Item>,
    val map: Map<String, Value>
)
```

---

### 2. Be Careful with Nullable Complex Objects

```kotlin
// Problem: Nullable complex objects can cause unnecessary recompositions
data class UiState(
    val searchResponse: SearchMultiMovieDto? = null
)

// Solution: Use remember to stabilize
@Composable
fun Content(state: UiState) {
    val results = remember(state.searchResponse) {
        state.searchResponse?.result ?: emptyList()
    }
    LazyColumn(items = results) { ... }
}
```

---

### 3. Watch for Object Creation in Composables

**Bad:**
```kotlin
// Creates new list on every recomposition
LazyColumn(items = dataState.searchResponse?.result ?: emptyList())
```

**Good:**
```kotlin
// Remembers the list, only recreates when searchResponse changes
val items = remember(dataState.searchResponse) {
    dataState.searchResponse?.result ?: emptyList()
}
LazyColumn(items = items)
```

---

### 4. Avoid Creating Lambdas in State

```kotlin
// ❌ Bad - Lambda recreated on every state change
data class UiState(
    val onItemClick: (Item) -> Unit
)

// ✅ Good - Pass callbacks as parameters
@Composable
fun Screen(
    state: UiState,
    onItemClick: (Item) -> Unit
) { ... }
```

---

### 5. Handle Deep Object Comparisons

For nested objects, ensure proper equality:

```kotlin
// If SearchMultiMovieDto has nested mutable state, implement equals
data class SearchMultiMovieDto(
    val result: List<MovieTypeDTO>,
    val metaData: Metadata?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SearchMultiMovieDto) return false
        return result == other.result && metaData == other.metaData
    }
}
```

---

## Alternative Approaches

### 1. Multiple State Flows (Separation of Concerns)

**Implementation:**
```kotlin
class MainHomeScreenViewModel {
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()
    
    private val _searchResponse = MutableStateFlow<SearchMultiMovieDto?>(null)
    val searchResponse: StateFlow<SearchMultiMovieDto?> = _searchResponse.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
}
```

**Usage:**
```kotlin
@Composable
fun MainHomeScreen(
    viewModel: MainHomeScreenViewModel
) {
    val searchText by viewModel.searchText.collectAsStateWithLifecycle()
    val searchResponse by viewModel.searchResponse.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    
    // Only recomposes when specific property changes
    SearchBar(text = searchText, ...)
    ResultsList(items = searchResponse?.result ?: emptyList())
    if (isLoading) { LoadingIndicator() }
}
```

**Pros:**
- ✅ Granular recomposition control
- ✅ Only affected components recompose
- ✅ Better performance for large screens

**Cons:**
- ❌ More boilerplate
- ❌ Harder to maintain consistency
- ❌ More complex ViewModel

---

### 2. Sealed Class State (State Machine Pattern)

**Implementation:**
```kotlin
sealed class MainHomeScreenUiState {
    data object Idle : MainHomeScreenUiState()
    
    data class Searching(
        val query: String
    ) : MainHomeScreenUiState()
    
    data class Success(
        val query: String,
        val results: SearchMultiMovieDto
    ) : MainHomeScreenUiState()
    
    data class Error(
        val message: String,
        val query: String? = null
    ) : MainHomeScreenUiState()
}
```

**Usage:**
```kotlin
@Composable
fun MainHomeScreen(
    state: MainHomeScreenUiState
) {
    when (state) {
        is MainHomeScreenUiState.Idle -> {
            EmptyState()
        }
        is MainHomeScreenUiState.Searching -> {
            LoadingIndicator()
            SearchBar(query = state.query)
        }
        is MainHomeScreenUiState.Success -> {
            ResultsList(results = state.results)
        }
        is MainHomeScreenUiState.Error -> {
            ErrorMessage(message = state.message)
        }
    }
}
```

**Pros:**
- ✅ Type-safe state transitions
- ✅ Impossible to have invalid states
- ✅ Clear state machine representation

**Cons:**
- ❌ Can be verbose for simple states
- ❌ Requires pattern matching everywhere

---

### 3. Nested State Classes (Hierarchical)

**Implementation:**
```kotlin
data class MainHomeScreenUiState(
    val search: SearchState = SearchState(),
    val results: ResultsState = ResultsState(),
    val loading: LoadingState = LoadingState()
) {
    data class SearchState(
        val text: String = "",
        val filter: Filter? = null
    )
    
    data class ResultsState(
        val data: SearchMultiMovieDto? = null,
        val errorMessage: String? = null
    )
    
    data class LoadingState(
        val isLoading: Boolean = false,
        val progress: Float = 0f
    )
}
```

**Usage:**
```kotlin
@Composable
fun MainContent(state: MainHomeScreenUiState) {
    SearchSection(
        text = state.search.text,
        filter = state.search.filter
    )
    ResultsSection(
        data = state.results.data,
        error = state.results.errorMessage
    )
    LoadingIndicator(
        isLoading = state.loading.isLoading,
        progress = state.loading.progress
    )
}
```

**Pros:**
- ✅ Logical grouping of related state
- ✅ Easier to manage large states
- ✅ Better organization

**Cons:**
- ❌ More nested access
- ❌ Still has recomposition issues if not handled properly

---

### 4. State Holder Pattern (Compose)

**Implementation:**
```kotlin
@Stable
class MainHomeScreenStateHolder {
    var searchText by mutableStateOf("")
        private set
    
    var searchResponse by mutableStateOf<SearchMultiMovieDto?>(null)
        private set
    
    var isLoading by mutableStateOf(false)
        private set
    
    fun updateSearchText(text: String) {
        searchText = text
    }
    
    fun updateSearchResponse(response: SearchMultiMovieDto?) {
        searchResponse = response
    }
    
    fun setLoading(loading: Boolean) {
        isLoading = loading
    }
}
```

**Usage:**
```kotlin
@Composable
fun MainHomeScreen(
    stateHolder: MainHomeScreenStateHolder
) {
    SearchBar(text = stateHolder.searchText) {
        stateHolder.updateSearchText(it)
    }
    ResultsList(items = stateHolder.searchResponse?.result ?: emptyList())
    if (stateHolder.isLoading) { LoadingIndicator() }
}
```

**Pros:**
- ✅ Fine-grained recomposition control
- ✅ Encapsulated state updates
- ✅ Can add computed properties easily

**Cons:**
- ❌ More boilerplate
- ❌ Not as type-safe as data classes
- ❌ Requires careful implementation

---

## Recommended Implementation

For the current `MainHomeScreenUiState` implementation, here's the recommended approach:

### 1. Keep Single UiState (Current Approach is Good)

The current state is small and manageable:
```kotlin
data class MainHomeScreenUiState(
    val searchText: String = ExtConstants.StringConstants.EMPTY,
    val searchResponse: SearchMultiMovieDto? = null,
    val isLoading: Boolean = false
)
```

### 2. Optimize MainContent with Destructuring

```kotlin
@Composable
private fun MainContent(
    dataState: MainHomeScreenUiState,
    onIntent: (MainHomeScreenUiIntents) -> Unit
) {
    // Destructure to enable granular recomposition tracking
    val searchText = dataState.searchText
    val searchResponse = dataState.searchResponse
    val isLoading = dataState.isLoading
    
    // Rest of implementation...
}
```

### 3. Extract Composable Functions

```kotlin
@Composable
private fun SearchSection(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    SearchBar(
        text = searchText,
        onValueChange = onSearchTextChange
    ) { ... }
    
    PrimaryButton(onClick = onSearchClick) { ... }
}

@Composable
private fun ResultsSection(
    searchResponse: SearchMultiMovieDto?,
    onItemClick: (Media) -> Unit,
    onLoadMore: () -> Unit
) {
    val items = remember(searchResponse) {
        searchResponse?.result ?: emptyList()
    }
    
    LazyColumn(
        items = items,
        key = { item -> item.mediaType }
    ) { item ->
        MovieCarousal(
            data = item,
            onClick = { onItemClick(it) },
            onLoadMore = onLoadMore
        )
    }
}

@Composable
private fun LoadingIndicator(
    isLoading: Boolean,
    showOnlyOnFirstPage: Boolean = true,
    currentPage: Int? = null
) {
    if (isLoading && (!showOnlyOnFirstPage || (currentPage ?: 1) <= 1)) {
        CircularProgressIndicator(...)
    }
}
```

### 4. Optimize ViewModel Updates

```kotlin
// In ViewModel - batch updates when possible
fun onIntent(intent: MainHomeScreenUiIntents) {
    when (intent) {
        is MainHomeScreenUiIntents.TextFieldsIntent.OnSearchTextUpdate -> {
            _state.update { it.copy(searchText = intent.search) }
        }
        is MainHomeScreenUiIntents.ButtonIntents.OnSearchButtonIntent -> {
            // Batch loading state with search reset
            _state.update { 
                it.copy(
                    searchResponse = null,
                    isLoading = true
                )
            }
            searchMulti(resetResults = true)
        }
        // ...
    }
}
```

### 5. Use Stable Keys in Lists

```kotlin
LazyColumn(
    items = items,
    key = { item -> item.mediaType } // Stable, unique key
) { item ->
    // ...
}
```

---

## Summary

### When to Use Single UiState:
- ✅ Small to medium-sized screens (5-10 properties)
- ✅ Related state properties
- ✅ Need for atomic state updates
- ✅ Simpler state management

### When to Consider Alternatives:
- ❌ Large screens with many unrelated properties
- ❌ Performance-critical screens
- ❌ Complex state machines
- ❌ Need for granular recomposition control

### Key Takeaways:
1. **Destructure state properties** to enable granular recomposition
2. **Extract composables** with specific parameters
3. **Use `remember`** for computed values and stable references
4. **Minimize state updates** by batching changes
5. **Use stable keys** in LazyColumn/LazyRow
6. **Keep state classes small** and focused
7. **Separate UI state from business data**

---

## Additional Resources

- [Jetpack Compose State Documentation](https://developer.android.com/jetpack/compose/state)
- [Compose Performance Best Practices](https://developer.android.com/jetpack/compose/performance)
- [State and Jetpack Compose](https://developer.android.com/jetpack/compose/state-hoisting)
- [Recomposition in Compose](https://developer.android.com/jetpack/compose/mental-model#recomposition)

---

*Last Updated: 2024*

