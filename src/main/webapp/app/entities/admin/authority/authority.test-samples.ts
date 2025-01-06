import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '9b46811c-79f7-48b3-90ee-7c40fb1a1b4d',
};

export const sampleWithPartialData: IAuthority = {
  name: '761087f0-8583-4677-9aba-d51cc1abf010',
};

export const sampleWithFullData: IAuthority = {
  name: '9b763475-64e3-4913-8af6-7e7b8c0cd858',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
