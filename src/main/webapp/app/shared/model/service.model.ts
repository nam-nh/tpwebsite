export interface IService {
  id?: number;
  serviceName?: string;
  serviceDescription?: string;
  serviceImage?: string;
}

export const defaultValue: Readonly<IService> = {};
