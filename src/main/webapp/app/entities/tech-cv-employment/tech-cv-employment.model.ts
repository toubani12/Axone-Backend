import { ITechnicalCV } from 'app/entities/technical-cv/technical-cv.model';

export interface ITechCVEmployment {
  id: number;
  employment?: string | null;
  technicalCV?: ITechnicalCV | null;
}

export type NewTechCVEmployment = Omit<ITechCVEmployment, 'id'> & { id: null };
