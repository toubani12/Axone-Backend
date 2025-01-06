import { IEmployer } from 'app/entities/employer/employer.model';
import { IApplication } from 'app/entities/application/application.model';
import { TemplateContractType } from 'app/entities/enumerations/template-contract-type.model';

export interface ITemplate {
  id: number;
  label?: string | null;
  type?: keyof typeof TemplateContractType | null;
  description?: string | null;
  standard?: boolean | null;
  docLink?: string | null;
  docLinkContentType?: string | null;
  owner?: IEmployer | null;
  applications?: IApplication[] | null;
}

export type NewTemplate = Omit<ITemplate, 'id'> & { id: null };
