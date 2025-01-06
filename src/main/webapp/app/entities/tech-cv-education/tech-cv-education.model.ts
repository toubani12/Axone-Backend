import { ITechnicalCV } from 'app/entities/technical-cv/technical-cv.model';

export interface ITechCVEducation {
  id: number;
  education?: string | null;
  technicalCV?: ITechnicalCV | null;
}

export type NewTechCVEducation = Omit<ITechCVEducation, 'id'> & { id: null };
