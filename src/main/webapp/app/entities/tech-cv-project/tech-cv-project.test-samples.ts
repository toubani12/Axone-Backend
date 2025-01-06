import { ITechCVProject, NewTechCVProject } from './tech-cv-project.model';

export const sampleWithRequiredData: ITechCVProject = {
  id: 31231,
  project: 'bubbly untie',
};

export const sampleWithPartialData: ITechCVProject = {
  id: 18094,
  project: 'instead wrinkle',
};

export const sampleWithFullData: ITechCVProject = {
  id: 26663,
  project: 'pfft yearn wrestler',
};

export const sampleWithNewData: NewTechCVProject = {
  project: 'disband imperturbable headline',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
