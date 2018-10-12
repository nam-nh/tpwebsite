export interface IProject {
  id?: number;
  projectName?: string;
  minSalary?: number;
  maxSalary?: number;
}

export const defaultValue: Readonly<IProject> = {};
