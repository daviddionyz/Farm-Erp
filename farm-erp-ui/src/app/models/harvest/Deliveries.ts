import {Field} from "../fields/field";
import {Storage} from "../storage/storage";
import {Vehicles} from "../vehicles/vehicles";
import {Worker} from "../worker/worker";
import {Crops} from "../storage/Crops";

export interface Deliveries{

  id : number
  harvest_diary : number
  gross       : number
  empty       : number
  net         : number
  worker      : Worker
  vehicle     : Vehicles
  intakeDate  : Date
  from        : Field
  fromStorage : Storage
  where       : Storage
  isCorpMoving: boolean
  crop        : Crops
}
