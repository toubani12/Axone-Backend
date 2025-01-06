import { IDomain, NewDomain } from './domain.model';

export const sampleWithRequiredData: IDomain = {
  id: 7461,
  name: 'examine lest confer',
};

export const sampleWithPartialData: IDomain = {
  id: 20218,
  name: 'which tightly',
};

export const sampleWithFullData: IDomain = {
  id: 31308,
  name: 'readily',
};

export const sampleWithNewData: NewDomain = {
  name: 'lighthearted vaguely',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
