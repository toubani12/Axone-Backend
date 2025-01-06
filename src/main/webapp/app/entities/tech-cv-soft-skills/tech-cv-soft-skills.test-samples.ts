import { ITechCVSoftSkills, NewTechCVSoftSkills } from './tech-cv-soft-skills.model';

export const sampleWithRequiredData: ITechCVSoftSkills = {
  id: 10130,
  skills: 'sticker that',
};

export const sampleWithPartialData: ITechCVSoftSkills = {
  id: 15364,
  skills: 'provided meaningfully',
};

export const sampleWithFullData: ITechCVSoftSkills = {
  id: 31608,
  skills: 'sprint',
};

export const sampleWithNewData: NewTechCVSoftSkills = {
  skills: 'merit',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
