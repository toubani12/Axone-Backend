import { IEmployer, NewEmployer } from './employer.model';

export const sampleWithRequiredData: IEmployer = {
  id: 30533,
  role: 'RECRUITER',
  status: 'BANNED',
  label: 'please prune whoa',
};

export const sampleWithPartialData: IEmployer = {
  id: 19808,
  lastName: 'Treutel',
  profileImage: '../fake-data/blob/hipster.png',
  profileImageContentType: 'unknown',
  address: 'initialize retest',
  role: 'EMPLOYER',
  status: 'DEACTIVATED',
  label: 'whose provided',
};

export const sampleWithFullData: IEmployer = {
  id: 10333,
  firstName: 'Carley',
  lastName: 'Mante',
  profileImage: '../fake-data/blob/hipster.png',
  profileImageContentType: 'unknown',
  address: 'happily',
  role: 'ADMIN',
  status: 'DEACTIVATED',
  name: 'hence speedily',
  label: 'eek',
};

export const sampleWithNewData: NewEmployer = {
  role: 'EMPLOYER',
  status: 'DEACTIVATED',
  label: 'nervous',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
