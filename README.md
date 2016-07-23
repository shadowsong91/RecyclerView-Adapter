# RecyclerView-Adapter
This is an attempt to make RecyclerView more easier to use. The custom Adapter implements loading view when loading data, error view when error occurs and load more view when having more data.
# How to use
1. Implement BaseViewAdapter and override onCreat**(exclueded onCreateViewHolder) function. Override useErrorHolder() (just return true)if you want a error view when error occurs. Override usePlaceholder() (just return true)if you want a placeholder when data is not prepared. And you can set view count by overriding getErrorHolderCount and getPlaceholderCount.
2. Use a ListWrapper to load data.
3. Set ListWrapper to your adapter.
4. ListWrapper.load().

# Example
See MainActivity.java
