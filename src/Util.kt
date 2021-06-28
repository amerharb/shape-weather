package com.amerharb.shape

object Util {
    fun getLongFromEnvVar(varName:String, default: Long): Long{
        return try {
            System.getenv(varName).toLong()
        } catch (e:Exception) {
            default
        }
    }

    fun getStringFromEnvVar(varName:String, default: String): String{
        return try {
            System.getenv(varName) ?: default
        } catch (e: Exception) {
            default
        }
    }
}
