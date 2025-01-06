import { ITechCVHardSkills, NewTechCVHardSkills } from './tech-cv-hard-skills.model';

export const sampleWithRequiredData: ITechCVHardSkills = {
  id: 22200,
  skills: 'since stumble',
};

export const sampleWithPartialData: ITechCVHardSkills = {
  id: 15679,
  skills: 'movies',
};

export const sampleWithFullData: ITechCVHardSkills = {
  id: 2576,
  skills: 'unionize vivaciously stepdaughter',
};

export const sampleWithNewData: NewTechCVHardSkills = {
  skills: 'indeed excitable',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
