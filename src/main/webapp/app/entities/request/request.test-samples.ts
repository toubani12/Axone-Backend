import { IRequest, NewRequest } from './request.model';

export const sampleWithRequiredData: IRequest = {
  id: 14568,
  status: 'REVOKED',
  expressionOfInterest: 'meh',
};

export const sampleWithPartialData: IRequest = {
  id: 6778,
  status: 'PROCESSING',
  expressionOfInterest: 'boo stench per',
};

export const sampleWithFullData: IRequest = {
  id: 28641,
  status: 'REVOKED',
  expressionOfInterest: 'boo',
};

export const sampleWithNewData: NewRequest = {
  status: 'PROCESSING',
  expressionOfInterest: 'into supposing',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
