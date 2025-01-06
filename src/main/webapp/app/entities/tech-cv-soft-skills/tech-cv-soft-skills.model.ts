import { ITechnicalCV } from 'app/entities/technical-cv/technical-cv.model';

export interface ITechCVSoftSkills {
  id: number;
  skills?: string | null;
  technicalCV?: ITechnicalCV | null;
}

export type NewTechCVSoftSkills = Omit<ITechCVSoftSkills, 'id'> & { id: null };
