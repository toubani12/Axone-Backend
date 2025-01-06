import { ITechnicalCV } from 'app/entities/technical-cv/technical-cv.model';

export interface ITechCVHardSkills {
  id: number;
  skills?: string | null;
  technicalCV?: ITechnicalCV | null;
}

export type NewTechCVHardSkills = Omit<ITechCVHardSkills, 'id'> & { id: null };
