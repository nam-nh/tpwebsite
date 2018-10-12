import { IItemGroup } from 'app/shared/model//item-group.model';

export interface ICategory {
  id?: number;
  categoryName?: string;
  categoryDescription?: string;
  itemGroups?: IItemGroup[];
}

export const defaultValue: Readonly<ICategory> = {};
