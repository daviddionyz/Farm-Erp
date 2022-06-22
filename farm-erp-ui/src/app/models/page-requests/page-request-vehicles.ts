import {PageRequestBase} from "./page-request-base";

export interface PageRequestVehicles extends PageRequestBase{
  name  : string;
  type  : string;
  status: number;
}
