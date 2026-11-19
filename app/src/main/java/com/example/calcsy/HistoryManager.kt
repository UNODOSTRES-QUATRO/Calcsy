package com.example.calcsy

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

object HistoryManager {
    private const val PREF_NAME = "calcsy_history_pref"
    private const val KEY_LIST = "history_list"

    fun addHistory(context: Context, expression: String, result: String) {
        val list = getHistory(context).toMutableList()
        list.add(0, HistoryItem(expression, result))
        saveList(context, list)
    }

    fun getHistory(context: Context): List<HistoryItem> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val jsonString = prefs.getString(KEY_LIST, "[]") ?: "[]"
        val jsonArray = JSONArray(jsonString)
        val result = mutableListOf<HistoryItem>()

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            result.add(
                HistoryItem(
                    expression = obj.getString("expression"),
                    result = obj.getString("result")
                )
            )
        }
        return result
    }

    fun clearHistory(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_LIST).apply()
    }

    private fun saveList(context: Context, list: List<HistoryItem>) {
        val jsonArray = JSONArray()
        for (item in list) {
            val obj = JSONObject()
            obj.put("expression", item.expression)
            obj.put("result", item.result)
            jsonArray.put(obj)
        }
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LIST, jsonArray.toString()).apply()
    }
}