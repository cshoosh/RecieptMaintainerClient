package com.example.shahnawaz.recieptmaintainer.model

/**
 * Created by Shahnawaz on 6/13/2016.
 */

data class Data(val _id: Int, val amount: Int, val description: String, val date: String)

data class ListData(val list: List<Data>)
