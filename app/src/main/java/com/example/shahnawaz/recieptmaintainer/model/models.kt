package com.example.shahnawaz.recieptmaintainer.model

/**
 * Created by Shahnawaz on 6/13/2016.
 */

data class Data(val _id: Int, val amount: Int, val description: String, val date: String, val credit: Int)

data class ListData(val list: List<Data>)

data class CalculateModel(val diff: Int, val sum_credit: Int, val sum_paid: Int)