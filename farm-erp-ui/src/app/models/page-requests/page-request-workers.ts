import {PageRequestBase} from "./page-request-base";

export interface PageRequestWorkers extends PageRequestBase{
  name          : string;
  vehicle       : number,
  position      : string,

  createdAtFrom : string,
  createdAtTo   : string
}
