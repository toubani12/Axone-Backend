import { ITechnicalCV } from 'app/entities/technical-cv/technical-cv.model';

export interface ITechCVProject {
  id: number;
  project?: string | null;
  technicalCV?: ITechnicalCV | null;
}

export type NewTechCVProject = Omit<ITechCVProject, 'id'> & { id: null };
