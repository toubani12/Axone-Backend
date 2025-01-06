import { ICriteria, NewCriteria } from './criteria.model';

export const sampleWithRequiredData: ICriteria = {
  id: 4939,
  name: 'eve',
};

export const sampleWithPartialData: ICriteria = {
  id: 30299,
  name: 'inter',
};

export const sampleWithFullData: ICriteria = {
  id: 23153,
  name: 'phooey though forenenst',
};

export const sampleWithNewData: NewCriteria = {
  name: 'however',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
