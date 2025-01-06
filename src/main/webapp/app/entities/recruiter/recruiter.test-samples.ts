import { IRecruiter, NewRecruiter } from './recruiter.model';

export const sampleWithRequiredData: IRecruiter = {
  id: 19595,
  role: 'RECRUITER',
  status: 'ACTIVE',
  label: 'lilac bathroom mysteriously',
  linkedinUrl: 'politely',
};

export const sampleWithPartialData: IRecruiter = {
  id: 4466,
  firstName: 'Gunnar',
  lastName: 'Harber',
  profileImage: '../fake-data/blob/hipster.png',
  profileImageContentType: 'unknown',
  role: 'ADMIN',
  status: 'DEACTIVATED',
  name: 'elegantly',
  label: 'wick while',
  linkedinUrl: 'impressionable pitch parade',
  approvedExperience: true,
};

export const sampleWithFullData: IRecruiter = {
  id: 7230,
  firstName: 'Rogelio',
  lastName: 'Kerluke',
  profileImage: '../fake-data/blob/hipster.png',
  profileImageContentType: 'unknown',
  address: 'whenever meet hypochondria',
  role: 'EMPLOYER',
  status: 'BANNED',
  name: 'now',
  label: 'now ha mutter',
  linkedinUrl: 'bah crane',
  approvedExperience: true,
  score: 2662.77,
};

export const sampleWithNewData: NewRecruiter = {
  role: 'EMPLOYER',
  status: 'BANNED',
  label: 'eek',
  linkedinUrl: 'swill',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
