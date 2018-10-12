import { IItemSubGroup } from 'app/shared/model//item-sub-group.model';

export interface IItem {
  id?: number;
  itemName?: string;
  itemImage?: string;
  itemDescription?: string;
  itemQuantity?: number;
  itemPrice?: number;
  itemSubGroup?: IItemSubGroup;
}

export const defaultValue: Readonly<IItem> = {};
