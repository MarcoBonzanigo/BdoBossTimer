package com.scythetec.bdobosstimer.helper

interface IBossSettings {
    fun toBossSettings(): BossSettings? {
        if (this is BossSettings){
            return this
        }else if (this is BossTestSettings){
            return this.convertToBossSettings()
        }
        return null
    }
}