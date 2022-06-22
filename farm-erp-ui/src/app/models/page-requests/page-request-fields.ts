import {PageRequestBase} from "./page-request-base";

export interface PageRequestFields extends PageRequestBase{
  name      : string;
  corpType  : string;
  corpName  : string;
}
