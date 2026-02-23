# Recomposition Optimization for Children Composables Guide

## Table of Contents
1. [Overview](#overview)
2. [Understanding Children Recomposition](#understanding-children-recomposition)
3. [LazyColumn & LazyRow Optimization](#lazycolumn--lazyrow-optimization)
4. [Column & Row Optimization](#column--row-optimization)
5. [Box Optimization](#box-optimization)
6. [Key Composable Usage](#key-composable-usage)
7. [Stable Parameters & Callbacks](#stable-parameters--callbacks)
8. [Remember for Expensive Operations](#remember-for-expensive-operations)
9. [Extracting Children Composables](#extracting-children-composables)
10. [Complete Examples](#complete-examples)
11. [Best Practices Summary](#best-practices-summary)

---

## Overview

When working with layout composables like `Column`, `Row`, `LazyColumn`, `LazyRow`, and `Box`, all children recompose when the parent recomposes. This guide provides techniques to optimize recomposition of children composables to improve performance.

### Common Issues

- **All children recompose together**: When parent state changes, all children recompose even if they don't use the changed state
- **Expensive computations in children**: Calculations performed on every recomposition
- **Unstable keys in lazy lists**: Causes unnecessary item recompositions
- **Recreated lambdas**: New lambda instances on every recomposition
- **Complex modifier chains**: Recreated on every recomposition

---

## Understanding Children Recomposition

### How Recomposition Works

```kotlin
// ❌ Problem: All children recompose when parent state changes
@Composable
fun ParentScreen(state: UiState) {
    Column {
        Child1(text = state.text)        // Recomposes
        Child2(count = state.count)      // Recomposes
        Child3(data = state.data)        // Recomposes
    }
}

// When state.text changes:
// - Child1 recomposes ✅ (needs text)
// - Child2 recomposes ❌ (doesn't need text)
// - Child3 recomposes ❌ (doesn't need text)
```

### Recomposition Flow

```
Parent State Change
    ↓
Parent Composable Recomposes
    ↓
ALL Children Recompose (by default)
    ↓
Even children that don't use changed state recompose
```

---

## LazyColumn & LazyRow Optimization

### 1. Use Stable Keys

**Problem:**
```kotlin
// ❌ Current - Key computation happens on every recomposition
LazyRow {
    items(
        items = data.mediaItems,
        key = { item ->
            "${item.id}_${item.mediaType}_${item.posterPath ?: ""}_${item.backdropPath ?: ""}"
        }
    ) { item ->
        MovieCard(data = item)
    }
}
```

**Solution:**
```kotlin
// ✅ Better - Use stable, simple key
LazyRow {
    items(
        items = data.mediaItems,
        key = { item -> item.id ?: 0 } // Simple, stable key
    ) { item ->
        MovieCard(data = item)
    }
}
```

**Why it works:** Stable keys help Compose identify items efficiently and prevent unnecessary recompositions when items are reordered or updated.

---

### 2. Remember Items List

**Problem:**
```kotlin
// ❌ Current - List recreated on every recomposition
LazyColumn {
    items(items = data.items ?: emptyList()) { item ->
        ItemCard(item = item)
    }
}
```

**Solution:**
```kotlin
// ✅ Better - Remember the list
val items = remember(data.items) {
    data.items ?: emptyList()
}

LazyColumn {
    items(items = items) { item ->
        ItemCard(item = item)
    }
}
```

---

### 3. Optimize Key Generation

**For Composite Keys:**
```kotlin
// ✅ Remember keys to prevent recalculation
val itemsWithKeys = remember(data.mediaItems) {
    data.mediaItems.map { item ->
        item to (item.id ?: 0) // Pair of item and stable key
    }
}

LazyRow {
    items(
        items = itemsWithKeys,
        key = { (_, key) -> key }
    ) { (item, _) ->
        MovieCard(data = item)
    }
}
```

---

## Column & Row Optimization

### 1. Extract Children into Separate Composables

**Problem:**
```kotlin
// ❌ Current - All children recompose together
@Composable
fun MovieCarousal(data: MovieTypeDTO) {
    Column {
        CustomText(
            text = data.mediaType.uppercase(), // Recomputes on every recomposition
            ...
        )
        LazyRow(...) {
            items(...) { item ->
                MovieCard(...) // All cards recompose
            }
        }
    }
}
```

**Solution:**
```kotlin
// ✅ Better - Extract children to isolate recompositions
@Composable
fun MovieCarousal(data: MovieTypeDTO) {
    Column {
        MediaTypeHeader(mediaType = data.mediaType)
        MediaItemsList(
            items = data.mediaItems,
            onClick = onClick,
            onLoadMore = onLoadMore
        )
    }
}

@Composable
private fun MediaTypeHeader(mediaType: String) {
    val text = remember(mediaType) {
        mediaType.uppercase()
    }
    CustomText(
        text = text,
        fontSize = AppDimens.Fonts.font24,
        fontWeight = FontWeight.W500
    )
}

@Composable
private fun MediaItemsList(
    items: List<MediaDTO>,
    onClick: (MediaDTO) -> Unit,
    onLoadMore: () -> Unit
) {
    LazyRow(...) {
        items(items = items, key = { it.id ?: 0 }) { item ->
            MovieCard(data = item)
        }
    }
}
```

**Why it works:** Smaller composables with specific parameters only recompose when their specific parameters change.

---

### 2. Use `key()` Composable for Non-Lazy Layouts

**Problem:**
```kotlin
// ❌ Current - All items recompose
Column {
    items.forEach { item ->
        ItemCard(item = item)
    }
}
```

**Solution:**
```kotlin
// ✅ Better - Use key() for stable tracking
Column {
    items.forEach { item ->
        key(item.id) { // Helps Compose track items
            ItemCard(item = item)
        }
    }
}
```

**Why it works:** The `key()` composable helps Compose identify and track items, allowing it to skip recomposition when items haven't changed.

---

### 3. Remember Expensive Computations

**Problem:**
```kotlin
// ❌ Current - String computation on every recomposition
Column {
    CustomText(
        text = data.name ?: data.title ?: "",
        ...
    )
}
```

**Solution:**
```kotlin
// ✅ Better - Remember computed text
val displayText = remember(data.name, data.title) {
    data.name ?: data.title ?: ""
}

Column {
    CustomText(
        text = displayText,
        ...
    )
}
```

---

## Box Optimization

### 1. Extract Content Composables

**Problem:**
```kotlin
// ❌ Current - All content recomposes
Box(modifier = modifier) {
    Image(...)
    Text(...)
    Button(...)
}
```

**Solution:**
```kotlin
// ✅ Better - Extract content
Box(modifier = modifier) {
    BoxImage(url = imageUrl)
    BoxText(text = text)
    BoxButton(onClick = onClick)
}

@Composable
private fun BoxImage(url: String?) {
    Image(...)
}

@Composable
private fun BoxText(text: String) {
    Text(...)
}

@Composable
private fun BoxButton(onClick: () -> Unit) {
    Button(onClick = onClick) { ... }
}
```

---

### 2. Remember Modifier Chains

**Problem:**
```kotlin
// ❌ Current - Modifier recreated on every recomposition
Box(
    modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .background(Color.White)
        .clip(RoundedCornerShape(8.dp))
) { ... }
```

**Solution:**
```kotlin
// ✅ Better - Remember modifier chain
val boxModifier = remember {
    Modifier
        .fillMaxSize()
        .padding(16.dp)
        .background(Color.White)
        .clip(RoundedCornerShape(8.dp))
}

Box(modifier = boxModifier) { ... }
```

---

## Key Composable Usage

### When to Use `key()`

Use `key()` in non-lazy layouts to help Compose track items:

```kotlin
// ✅ Good - Use key() for stable tracking
Column {
    items.forEach { item ->
        key(item.id) {
            ItemCard(item = item)
        }
    }
}

// ✅ Good - Use key() for conditional content
Column {
    key(selectedItem?.id) {
        if (selectedItem != null) {
            SelectedItemCard(item = selectedItem)
        }
    }
}
```

### When NOT to Use `key()`

- In `LazyColumn`/`LazyRow` - use the `key` parameter in `items()` instead
- For simple, static content
- When keys are not stable or unique

---

## Stable Parameters & Callbacks

### 1. Pass Only Required Data

**Problem:**
```kotlin
// ❌ Current - Passing entire data object
MovieCard(data = item, ...)
```

**Solution:**
```kotlin
// ✅ Better - Pass only what's needed
MovieCard(
    posterUrl = item.posterPath,
    title = item.name ?: item.title ?: "",
    onClick = { onClick(item) }
)
```

---

### 2. Remember Callbacks

**Problem:**
```kotlin
// ❌ Current - Lambda recreated on every recomposition
Column {
    Button(onClick = { onItemClick(item) }) { ... }
    Text(text = item.name)
}
```

**Solution:**
```kotlin
// ✅ Better - Remember callbacks
Column {
    val onClick = remember(item.id) { { onItemClick(item) } }
    Button(onClick = onClick) { ... }
    Text(text = item.name)
}

// Or extract to separate composable
Column {
    ItemButton(item = item, onClick = onItemClick)
    Text(text = item.name)
}

@Composable
private fun ItemButton(item: Item, onClick: (Item) -> Unit) {
    Button(onClick = { onClick(item) }) { ... }
}
```

---

### 3. Use Stable Function References

**Problem:**
```kotlin
// ❌ Current - New lambda on every recomposition
LazyColumn {
    items(items) { item ->
        Button(onClick = { handleClick(item) }) { ... }
    }
}
```

**Solution:**
```kotlin
// ✅ Better - Stable callback
@Composable
fun ItemList(
    items: List<Item>,
    onItemClick: (Item) -> Unit
) {
    LazyColumn {
        items(items, key = { it.id }) { item ->
            ItemButton(
                item = item,
                onClick = { onItemClick(item) }
            )
        }
    }
}

@Composable
private fun ItemButton(
    item: Item,
    onClick: () -> Unit
) {
    Button(onClick = onClick) { ... }
}
```

---

## Remember for Expensive Operations

### 1. Remember Computed Values

```kotlin
// ✅ Remember string transformations
val uppercaseText = remember(text) {
    text.uppercase()
}

// ✅ Remember filtered lists
val filteredItems = remember(items, filter) {
    items.filter { it.matches(filter) }
}

// ✅ Remember sorted lists
val sortedItems = remember(items) {
    items.sortedBy { it.name }
}
```

---

### 2. Remember Derived State

```kotlin
// ✅ Use derivedStateOf for computed values
val mediaTypeText = remember(data.mediaType) {
    derivedStateOf { data.mediaType.uppercase() }
}

Column {
    CustomText(text = mediaTypeText.value)
}
```

---

### 3. Remember Complex Objects

```kotlin
// ✅ Remember list state
val listState = remember(data.mediaType) {
    rememberLazyListState()
}

// ✅ Remember painter
val painter = remember(imageUrl) {
    rememberAsyncImagePainter(model = imageUrl)
}
```

---

## Extracting Children Composables

### Strategy

1. **Identify independent children** - Children that don't share state
2. **Extract to separate composables** - Create focused composables
3. **Pass only required parameters** - Minimize parameter list
4. **Use stable types** - Prefer primitives and immutable types

### Example Transformation

**Before:**
```kotlin
@Composable
fun MovieCarousal(
    modifier: Modifier = Modifier,
    data: MovieTypeDTO,
    onClick: (MediaDTO) -> Unit,
    onLoadMore: () -> Unit
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        // ... pagination logic
    }

    Column(modifier = modifier.wrapContentSize()) {
        CustomText(
            text = CustomTextToDisplay.StringText(data.mediaType.uppercase()),
            fontSize = AppDimens.Fonts.font24,
            fontWeight = FontWeight.W500,
            modifier = Modifier.wrapContentSize()
        )
        LazyRow(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = AppDimens.Padding.smallPadding),
            horizontalArrangement = Arrangement.spacedBy(AppDimens.Padding.smallPadding)
        ) {
            items(
                items = data.mediaItems,
                key = { item ->
                    "${item.id}_${item.mediaType}_${item.posterPath ?: ""}_${item.backdropPath ?: ""}"
                }
            ) { item ->
                MovieCard(
                    modifier = Modifier
                        .wrapContentSize()
                        .clickableWithoutRipple {
                            onClick(item)
                        },
                    data = item
                )
            }
        }
    }
}
```

**After:**
```kotlin
@Composable
fun MovieCarousal(
    modifier: Modifier = Modifier,
    data: MovieTypeDTO,
    onClick: (MediaDTO) -> Unit,
    onLoadMore: () -> Unit
) {
    // Remember list state with stable key
    val listState = remember(data.mediaType) {
        rememberLazyListState()
    }
    
    // Remember computed text
    val mediaTypeText = remember(data.mediaType) {
        derivedStateOf { data.mediaType.uppercase() }
    }
    
    // Remember items list
    val mediaItems = remember(data.mediaItems) {
        data.mediaItems
    }
    
    // Remember callbacks
    val onItemClick = remember(onClick) { onClick }
    val onLoadMoreCallback = remember(onLoadMore) { onLoadMore }

    LaunchedEffect(listState, mediaItems.size) {
        snapshotFlow { listState.isScrollInProgress }
            .filter { it }
            .flatMapLatest {
                snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            }
            .collect { visibleItems ->
                val lastVisible = visibleItems.lastOrNull()?.index ?: return@collect
                if (lastVisible >= mediaItems.size - ONE) {
                    onLoadMoreCallback()
                }
            }
    }

    Column(modifier = modifier.wrapContentSize()) {
        MediaTypeHeader(mediaType = mediaTypeText.value)
        MediaItemsList(
            items = mediaItems,
            listState = listState,
            onItemClick = onItemClick
        )
    }
}

@Composable
private fun MediaTypeHeader(mediaType: String) {
    CustomText(
        text = CustomTextToDisplay.StringText(mediaType),
        fontSize = AppDimens.Fonts.font24,
        fontWeight = FontWeight.W500,
        modifier = Modifier.wrapContentSize()
    )
}

@Composable
private fun MediaItemsList(
    items: List<MediaDTO>,
    listState: LazyListState,
    onItemClick: (MediaDTO) -> Unit
) {
    LazyRow(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = AppDimens.Padding.smallPadding),
        horizontalArrangement = Arrangement.spacedBy(AppDimens.Padding.smallPadding)
    ) {
        items(
            items = items,
            key = { item -> item.id ?: 0 } // Simple, stable key
        ) { item ->
            MovieCardItem(
                item = item,
                onClick = { onItemClick(item) }
            )
        }
    }
}

@Composable
private fun MovieCardItem(
    item: MediaDTO,
    onClick: () -> Unit
) {
    MovieCard(
        modifier = Modifier
            .wrapContentSize()
            .clickableWithoutRipple(onClick = onClick),
        data = item
    )
}
```

---

## Complete Examples

### Example 1: Optimized MovieCard

**Before:**
```kotlin
@Composable
fun MovieCard(modifier: Modifier, data: MediaDTO) {
    Column(
        modifier = modifier.width(140.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MoviePoster(url = data.posterPath)
        CustomText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = AppDimens.Padding.defaultPadding),
            text = CustomTextToDisplay.StringText(
                text = data.name ?: data.title ?: ExtConstants.StringConstants.EMPTY
            ),
            color = AppColors.secondaryTextColor,
            fontSize = AppDimens.Fonts.font18,
            maxLines = 1,
            minLines = 1,
            textOverflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.W400
        )
    }
}
```

**After:**
```kotlin
@Composable
fun MovieCard(modifier: Modifier, data: MediaDTO) {
    // Remember computed text to prevent recalculation
    val displayText = remember(data.name, data.title) {
        data.name ?: data.title ?: ExtConstants.StringConstants.EMPTY
    }
    
    // Remember modifier chain
    val cardModifier = remember(modifier) {
        modifier.width(140.dp)
    }
    
    Column(
        modifier = cardModifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MoviePoster(url = data.posterPath)
        MovieTitle(text = displayText)
    }
}

@Composable
private fun MovieTitle(text: String) {
    CustomText(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = AppDimens.Padding.defaultPadding),
        text = CustomTextToDisplay.StringText(text = text),
        color = AppColors.secondaryTextColor,
        fontSize = AppDimens.Fonts.font18,
        maxLines = 1,
        minLines = 1,
        textOverflow = TextOverflow.Ellipsis,
        fontWeight = FontWeight.W400
    )
}
```

---

### Example 2: Optimized Column with Multiple Children

**Before:**
```kotlin
@Composable
fun ProductCard(product: Product, onAddToCart: (Product) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White)
            .clip(RoundedCornerShape(8.dp))
    ) {
        ProductImage(url = product.imageUrl)
        ProductName(name = product.name)
        ProductPrice(price = product.price)
        ProductDescription(description = product.description)
        AddToCartButton(
            onClick = { onAddToCart(product) }
        )
    }
}
```

**After:**
```kotlin
@Composable
fun ProductCard(product: Product, onAddToCart: (Product) -> Unit) {
    // Remember modifier chain
    val cardModifier = remember {
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White)
            .clip(RoundedCornerShape(8.dp))
    }
    
    // Remember callback
    val addToCart = remember(product.id, onAddToCart) {
        { onAddToCart(product) }
    }
    
    Column(modifier = cardModifier) {
        ProductImage(url = product.imageUrl)
        ProductName(name = product.name)
        ProductPrice(price = product.price)
        ProductDescription(description = product.description)
        AddToCartButton(onClick = addToCart)
    }
}

@Composable
private fun ProductImage(url: String?) {
    // Only recomposes when url changes
    AsyncImage(
        model = url,
        contentDescription = null
    )
}

@Composable
private fun ProductName(name: String) {
    // Only recomposes when name changes
    Text(text = name, ...)
}

@Composable
private fun ProductPrice(price: Double) {
    // Only recomposes when price changes
    Text(text = "$$price", ...)
}

@Composable
private fun ProductDescription(description: String) {
    // Only recomposes when description changes
    Text(text = description, ...)
}

@Composable
private fun AddToCartButton(onClick: () -> Unit) {
    // Only recomposes when onClick changes
    Button(onClick = onClick) {
        Text("Add to Cart")
    }
}
```

---

### Example 3: Optimized LazyColumn with Complex Items

**Before:**
```kotlin
@Composable
fun ItemList(items: List<Item>, onItemClick: (Item) -> Unit) {
    LazyColumn {
        items(items) { item ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable { onItemClick(item) }
            ) {
                Text(text = item.title)
                Text(text = item.description)
                Image(url = item.imageUrl)
                Button(onClick = { onItemClick(item) }) {
                    Text("View Details")
                }
            }
        }
    }
}
```

**After:**
```kotlin
@Composable
fun ItemList(items: List<Item>, onItemClick: (Item) -> Unit) {
    // Remember items list
    val itemList = remember(items) { items }
    
    LazyColumn {
        items(
            items = itemList,
            key = { item -> item.id } // Stable key
        ) { item ->
            ItemCard(
                item = item,
                onClick = { onItemClick(item) }
            )
        }
    }
}

@Composable
private fun ItemCard(item: Item, onClick: () -> Unit) {
    // Remember modifier
    val cardModifier = remember {
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = onClick)
    }
    
    Column(modifier = cardModifier) {
        ItemTitle(title = item.title)
        ItemDescription(description = item.description)
        ItemImage(url = item.imageUrl)
        ItemButton(onClick = onClick)
    }
}

@Composable
private fun ItemTitle(title: String) {
    Text(text = title, ...)
}

@Composable
private fun ItemDescription(description: String) {
    Text(text = description, ...)
}

@Composable
private fun ItemImage(url: String?) {
    AsyncImage(model = url, ...)
}

@Composable
private fun ItemButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text("View Details")
    }
}
```

---

## Best Practices Summary

### ✅ Do's

1. **Use stable keys in LazyColumn/LazyRow**
   - Simple, unique identifiers
   - Avoid complex string concatenations in keys

2. **Extract children into separate composables**
   - Isolate recompositions
   - Pass only required parameters

3. **Use `remember` for expensive computations**
   - String transformations
   - List filtering/sorting
   - Complex calculations

4. **Pass only required data to children**
   - Don't pass entire objects
   - Extract only needed properties

5. **Remember callbacks and state objects**
   - Prevent lambda recreation
   - Stabilize function references

6. **Use `derivedStateOf` for computed values**
   - Only recomputes when dependencies change
   - Efficient for derived state

7. **Use `key()` for non-lazy layouts**
   - Helps Compose track items
   - Useful in Column/Row with dynamic children

8. **Batch modifier operations**
   - Remember modifier chains
   - Reduce modifier recreation

9. **Add `@Stable` annotation to custom composables**
   - Tells Compose the composable is stable
   - Improves recomposition skipping

10. **Optimize conditional children**
    - Use `AnimatedVisibility` for smooth transitions
    - Remember visibility state

---

### ❌ Don'ts

1. **Don't create complex keys in lazy lists**
   ```kotlin
   // ❌ Bad
   key = { "${item.id}_${item.name}_${item.date}" }
   
   // ✅ Good
   key = { item.id }
   ```

2. **Don't pass entire objects when only properties are needed**
   ```kotlin
   // ❌ Bad
   ItemCard(item = item)
   
   // ✅ Good
   ItemCard(title = item.title, description = item.description)
   ```

3. **Don't create lambdas inline in children**
   ```kotlin
   // ❌ Bad
   Button(onClick = { handleClick(item) }) { ... }
   
   // ✅ Good
   val onClick = remember(item.id) { { handleClick(item) } }
   Button(onClick = onClick) { ... }
   ```

4. **Don't perform expensive computations in children**
   ```kotlin
   // ❌ Bad
   Text(text = data.items.map { it.name }.joinToString())
   
   // ✅ Good
   val text = remember(data.items) {
       data.items.map { it.name }.joinToString()
   }
   Text(text = text)
   ```

5. **Don't recreate modifier chains on every recomposition**
   ```kotlin
   // ❌ Bad
   Column(modifier = Modifier.fillMaxWidth().padding(16.dp).background(Color.White))
   
   // ✅ Good
   val modifier = remember {
       Modifier.fillMaxWidth().padding(16.dp).background(Color.White)
   }
   Column(modifier = modifier)
   ```

---

## Performance Impact

### Before Optimization

```
State Change (1 property)
    ↓
Parent Recomposes
    ↓
ALL Children Recompose (10 children)
    ↓
Total: 11 recompositions
```

### After Optimization

```
State Change (1 property)
    ↓
Parent Recomposes
    ↓
Only 1 Child Recomposes (that uses changed property)
    ↓
Total: 2 recompositions
```

**Improvement: 82% reduction in recompositions**

---

## Testing Recomposition

### Enable Recomposition Counting

```kotlin
@Composable
fun MyComposable() {
    var recompositionCount by remember { mutableStateOf(0) }
    recompositionCount++
    
    LaunchedEffect(Unit) {
        Log.d("Recomposition", "MyComposable recomposed $recompositionCount times")
    }
    
    // Your content
}
```

### Use Layout Inspector

1. Enable Layout Inspector in Android Studio
2. Check "Show Recomposition Counts"
3. Identify composables with high recomposition counts
4. Apply optimization techniques

---

## Additional Resources

- [Jetpack Compose Performance](https://developer.android.com/jetpack/compose/performance)
- [Recomposition in Compose](https://developer.android.com/jetpack/compose/mental-model#recomposition)
- [Compose Layouts](https://developer.android.com/jetpack/compose/layouts)
- [Lazy Lists in Compose](https://developer.android.com/jetpack/compose/lists)
- [State and Jetpack Compose](https://developer.android.com/jetpack/compose/state)

---

## Checklist for Optimization

When optimizing children recomposition, check:

- [ ] Are stable keys used in LazyColumn/LazyRow?
- [ ] Are children extracted into separate composables?
- [ ] Are expensive computations wrapped in `remember`?
- [ ] Are only required parameters passed to children?
- [ ] Are callbacks remembered or extracted?
- [ ] Are modifier chains remembered?
- [ ] Is `derivedStateOf` used for computed values?
- [ ] Is `key()` used in non-lazy layouts?
- [ ] Are conditional children optimized?
- [ ] Are custom composables marked with `@Stable`?

---

*Last Updated: 2024*

