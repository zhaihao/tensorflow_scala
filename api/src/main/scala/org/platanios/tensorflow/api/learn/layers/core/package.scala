/* Copyright 2017-18, Emmanouil Antonios Platanios. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.platanios.tensorflow.api.learn.layers

import org.platanios.tensorflow.api.ops.Output

/**
  * @author Emmanouil Antonios Platanios
  */
package object core {
  private[layers] trait API {
    def MLP(
        name: String,
        hiddenLayers: Seq[Int],
        outputSize: Int,
        activation: String => Layer[Output, Output] = (name: String) => ReLU(name, 0.1f)
    ): Layer[Output, Output] = {
      if (hiddenLayers.isEmpty) {
        Linear(s"$name/Linear", outputSize)
      } else {
        val size = hiddenLayers.head
        var layer = Linear(s"$name/Layer0/Linear", size) >> activation(s"$name/Layer0/Activation")
        hiddenLayers.zipWithIndex.tail.foreach(s => {
          layer = layer >> Linear(s"$name/Layer${s._2}/Linear", s._1) >> activation(s"$name/Layer${s._2}/Activation")
        })
        layer >> Linear("OutputLayer/Linear", outputSize)
      }
    }
  }
}
