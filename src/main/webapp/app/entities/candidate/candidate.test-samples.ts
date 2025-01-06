import { ICandidate, NewCandidate } from './candidate.model';

export const sampleWithRequiredData: ICandidate = {
  id: 13161,
  fullName: 'clam even',
  yearsOfExperience: 15330,
  hasContract: true,
  hired: false,
};

export const sampleWithPartialData: ICandidate = {
  id: 32293,
  fullName: 'gosh supervise messy',
  yearsOfExperience: 10477,
  hasContract: true,
  hired: true,
};

export const sampleWithFullData: ICandidate = {
  id: 27939,
  firstName: 'Gabe',
  lastName: 'Heidenreich',
  linkedinUrl: 'forenenst downfall',
  fullName: 'froth dovetail nervously',
  yearsOfExperience: 18203,
  currentSalary: 9970.5,
  desiredSalary: 677.62,
  hasContract: false,
  hired: false,
  rate: 12849.63,
};

export const sampleWithNewData: NewCandidate = {
  fullName: 'expedite winds bit',
  yearsOfExperience: 21772,
  hasContract: true,
  hired: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
