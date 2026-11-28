package com.estimote.indoorapp

import androidx.annotation.Keep

@Keep
data class DrawParams(var centerX : Double= 0.0,
                       var centerY : Double= 0.0,
                      var locationWidth : Double= 0.0,
                      var locationHeight : Double= 0.0,
                      var scale : Double= 0.0,
                      var viewHeight : Double= 0.0
                      ) {

}
