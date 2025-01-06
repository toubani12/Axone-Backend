import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IResume, NewResume } from '../resume.model';

export type PartialUpdateResume = Partial<IResume> & Pick<IResume, 'id'>;

export type EntityResponseType = HttpResponse<IResume>;
export type EntityArrayResponseType = HttpResponse<IResume[]>;

@Injectable({ providedIn: 'root' })
export class ResumeService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/resumes');

  create(resume: NewResume): Observable<EntityResponseType> {
    return this.http.post<IResume>(this.resourceUrl, resume, { observe: 'response' });
  }

  update(resume: IResume): Observable<EntityResponseType> {
    return this.http.put<IResume>(`${this.resourceUrl}/${this.getResumeIdentifier(resume)}`, resume, { observe: 'response' });
  }

  partialUpdate(resume: PartialUpdateResume): Observable<EntityResponseType> {
    return this.http.patch<IResume>(`${this.resourceUrl}/${this.getResumeIdentifier(resume)}`, resume, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IResume>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IResume[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getResumeIdentifier(resume: Pick<IResume, 'id'>): number {
    return resume.id;
  }

  compareResume(o1: Pick<IResume, 'id'> | null, o2: Pick<IResume, 'id'> | null): boolean {
    return o1 && o2 ? this.getResumeIdentifier(o1) === this.getResumeIdentifier(o2) : o1 === o2;
  }

  addResumeToCollectionIfMissing<Type extends Pick<IResume, 'id'>>(
    resumeCollection: Type[],
    ...resumesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const resumes: Type[] = resumesToCheck.filter(isPresent);
    if (resumes.length > 0) {
      const resumeCollectionIdentifiers = resumeCollection.map(resumeItem => this.getResumeIdentifier(resumeItem));
      const resumesToAdd = resumes.filter(resumeItem => {
        const resumeIdentifier = this.getResumeIdentifier(resumeItem);
        if (resumeCollectionIdentifiers.includes(resumeIdentifier)) {
          return false;
        }
        resumeCollectionIdentifiers.push(resumeIdentifier);
        return true;
      });
      return [...resumesToAdd, ...resumeCollection];
    }
    return resumeCollection;
  }
}
