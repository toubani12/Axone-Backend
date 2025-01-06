import { IProvider, NewProvider } from './provider.model';

export const sampleWithRequiredData: IProvider = {
  id: 29392,
  name: 'savage safely upon',
};

export const sampleWithPartialData: IProvider = {
  id: 1828,
  name: 'which uselessly dearly',
};

export const sampleWithFullData: IProvider = {
  id: 3887,
  name: 'boo chemistry for',
  country: 'Heard Island and McDonald Islands',
};

export const sampleWithNewData: NewProvider = {
  name: 'thicken now',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
