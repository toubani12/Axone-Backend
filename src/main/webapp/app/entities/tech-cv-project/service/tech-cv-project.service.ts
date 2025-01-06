import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITechCVProject, NewTechCVProject } from '../tech-cv-project.model';

export type PartialUpdateTechCVProject = Partial<ITechCVProject> & Pick<ITechCVProject, 'id'>;

export type EntityResponseType = HttpResponse<ITechCVProject>;
export type EntityArrayResponseType = HttpResponse<ITechCVProject[]>;

@Injectable({ providedIn: 'root' })
export class TechCVProjectService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tech-cv-projects');

  create(techCVProject: NewTechCVProject): Observable<EntityResponseType> {
    return this.http.post<ITechCVProject>(this.resourceUrl, techCVProject, { observe: 'response' });
  }

  update(techCVProject: ITechCVProject): Observable<EntityResponseType> {
    return this.http.put<ITechCVProject>(`${this.resourceUrl}/${this.getTechCVProjectIdentifier(techCVProject)}`, techCVProject, {
      observe: 'response',
    });
  }

  partialUpdate(techCVProject: PartialUpdateTechCVProject): Observable<EntityResponseType> {
    return this.http.patch<ITechCVProject>(`${this.resourceUrl}/${this.getTechCVProjectIdentifier(techCVProject)}`, techCVProject, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITechCVProject>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITechCVProject[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTechCVProjectIdentifier(techCVProject: Pick<ITechCVProject, 'id'>): number {
    return techCVProject.id;
  }

  compareTechCVProject(o1: Pick<ITechCVProject, 'id'> | null, o2: Pick<ITechCVProject, 'id'> | null): boolean {
    return o1 && o2 ? this.getTechCVProjectIdentifier(o1) === this.getTechCVProjectIdentifier(o2) : o1 === o2;
  }

  addTechCVProjectToCollectionIfMissing<Type extends Pick<ITechCVProject, 'id'>>(
    techCVProjectCollection: Type[],
    ...techCVProjectsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const techCVProjects: Type[] = techCVProjectsToCheck.filter(isPresent);
    if (techCVProjects.length > 0) {
      const techCVProjectCollectionIdentifiers = techCVProjectCollection.map(techCVProjectItem =>
        this.getTechCVProjectIdentifier(techCVProjectItem),
      );
      const techCVProjectsToAdd = techCVProjects.filter(techCVProjectItem => {
        const techCVProjectIdentifier = this.getTechCVProjectIdentifier(techCVProjectItem);
        if (techCVProjectCollectionIdentifiers.includes(techCVProjectIdentifier)) {
          return false;
        }
        techCVProjectCollectionIdentifiers.push(techCVProjectIdentifier);
        return true;
      });
      return [...techCVProjectsToAdd, ...techCVProjectCollection];
    }
    return techCVProjectCollection;
  }
}
