package word.from.words.domain

interface Keeper {
    fun getSharedUrl(): String?
    fun setSharedUrl(url:String)
    fun getSharedTo(): Boolean
    fun setSharedTo(to:Boolean)
}