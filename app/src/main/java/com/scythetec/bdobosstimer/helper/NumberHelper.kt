package com.scythetec.bdobosstimer.helper

class NumberHelper {

    companion object{
        val instance: NumberHelper =
            NumberHelper()
    }

    fun formatThousands(number: Int): String{
        val numberReturnRaw = (number*50000).toString()
        var numberReturn =""
        for (position in numberReturnRaw.indices){
            numberReturn = numberReturnRaw[numberReturnRaw.length-position-1] + (if (position%3==0 && position!=0) "'" else "")+numberReturn
        }
        return numberReturn
    }
}