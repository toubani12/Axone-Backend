import { IContract, NewContract } from './contract.model';

export const sampleWithRequiredData: IContract = {
  id: 11640,
  label: 'evergreen that',
  type: 'FREELANCE',
  status: 'ABORTED',
  directContract: true,
  paymentAmount: 19364.76,
  recruiterIncomeRate: 18334.71,
  candidateIncomeRate: 17124.14,
};

export const sampleWithPartialData: IContract = {
  id: 19300,
  label: 'that',
  type: 'REMOTE',
  status: 'CLOSED',
  directContract: true,
  paymentAmount: 20531.22,
  recruiterIncomeRate: 9275.87,
  candidateIncomeRate: 16938.89,
};

export const sampleWithFullData: IContract = {
  id: 18363,
  label: 'instead after',
  type: 'REMOTE',
  status: 'PROCESSING',
  directContract: false,
  paymentAmount: 17338.53,
  recruiterIncomeRate: 25575.19,
  candidateIncomeRate: 29449.12,
};

export const sampleWithNewData: NewContract = {
  label: 'around whoa',
  type: 'REMOTE',
  status: 'CREATED',
  directContract: true,
  paymentAmount: 14949.78,
  recruiterIncomeRate: 14773.46,
  candidateIncomeRate: 29991.15,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
