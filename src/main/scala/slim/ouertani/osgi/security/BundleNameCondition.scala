/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.osgi.osgi1

import java.util.Dictionary
import org.osgi.framework.Bundle
import org.osgi.service.condpermadmin.Condition
import org.osgi.service.condpermadmin.ConditionInfo

class BundleNameCondition(b : Bundle,  info:ConditionInfo*
) extends Condition
{
  //val action = info.getArgs()(0)
println ("BundleNameCondition------------")
  def  isSatisfied():Boolean= {
    println ("----------------")
    return true; }
  def  isPostponed():Boolean= {
    println ("------------------------------------------------------")
    return true; }
  def  isMutable():Boolean={ println ("-------------------------------------------")
                            return false; }
  def isSatisfied( conditions:Array[Condition],  state:Dictionary[_,_] ):Boolean={ println ("kkkk")
                                                                                  return true; }

}
