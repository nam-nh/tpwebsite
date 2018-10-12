import { IItemGroup } from 'app/shared/model//item-group.model';
import { IItem } from 'app/shared/model//item.model';

export interface IItemSubGroup {
  id?: number;
  itemSubGroupName?: string;
  itemSubGroupDescription?: string;
  itemGroup?: IItemGroup;
  items?: IItem[];
}

export const defaultValue: Readonly<IItemSubGroup> = {};
