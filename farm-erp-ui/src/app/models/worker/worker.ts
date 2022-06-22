import {Vehicles} from "../vehicles/vehicles";

export interface Worker {
  id:         number;
  name:       string;
  joinDate:   string;
  vehicle:  Vehicles | null;
  position:   string;
}
