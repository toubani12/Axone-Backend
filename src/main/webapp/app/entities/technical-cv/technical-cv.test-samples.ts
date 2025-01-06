import { ITechnicalCV, NewTechnicalCV } from './technical-cv.model';

export const sampleWithRequiredData: ITechnicalCV = {
  id: 15694,
  name: 'fishing phew chief',
  level: 'INTERMEDIATE',
};

export const sampleWithPartialData: ITechnicalCV = {
  id: 22066,
  name: 'wetsuit lazily adduce',
  level: 'EXPERT',
};

export const sampleWithFullData: ITechnicalCV = {
  id: 9768,
  name: 'chasten',
  level: 'EXPERT',
  profileDescription: 'frankly majestically',
};

export const sampleWithNewData: NewTechnicalCV = {
  name: 'ultimately',
  level: 'BEGINNER',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
