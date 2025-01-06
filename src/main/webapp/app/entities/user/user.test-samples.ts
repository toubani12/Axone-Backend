import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 6278,
  login: 'gb9kGh@v',
};

export const sampleWithPartialData: IUser = {
  id: 26919,
  login: '{Hm-@ivg',
};

export const sampleWithFullData: IUser = {
  id: 30547,
  login: '_-T',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
