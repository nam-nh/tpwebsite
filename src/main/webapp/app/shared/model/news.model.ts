export interface INews {
  id?: number;
  newsTitle?: string;
  newsContent?: string;
}

export const defaultValue: Readonly<INews> = {};
