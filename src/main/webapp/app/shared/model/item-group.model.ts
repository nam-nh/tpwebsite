import { ICategory } from 'app/shared/model//category.model';
import { IItemSubGroup } from 'app/shared/model//item-sub-group.model';

export interface IItemGroup {
  id?: number;
  itemGroupName?: string;
  itemGroupDescription?: string;
  category?: ICategory;
  itemSubGroups?: IItemSubGroup[];
}

export const defaultValue: Readonly<IItemGroup> = {};
