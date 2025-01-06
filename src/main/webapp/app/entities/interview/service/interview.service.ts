import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInterview, NewInterview } from '../interview.model';

export type PartialUpdateInterview = Partial<IInterview> & Pick<IInterview, 'id'>;

type RestOf<T extends IInterview | NewInterview> = Omit<T, 'startedAt' | 'endedAt'> & {
  startedAt?: string | null;
  endedAt?: string | null;
};

export type RestInterview = RestOf<IInterview>;

export type NewRestInterview = RestOf<NewInterview>;

export type PartialUpdateRestInterview = RestOf<PartialUpdateInterview>;

export type EntityResponseType = HttpResponse<IInterview>;
export type EntityArrayResponseType = HttpResponse<IInterview[]>;

@Injectable({ providedIn: 'root' })
export class InterviewService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/interviews');

  create(interview: NewInterview): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(interview);
    return this.http
      .post<RestInterview>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(interview: IInterview): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(interview);
    return this.http
      .put<RestInterview>(`${this.resourceUrl}/${this.getInterviewIdentifier(interview)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(interview: PartialUpdateInterview): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(interview);
    return this.http
      .patch<RestInterview>(`${this.resourceUrl}/${this.getInterviewIdentifier(interview)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestInterview>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestInterview[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getInterviewIdentifier(interview: Pick<IInterview, 'id'>): number {
    return interview.id;
  }

  compareInterview(o1: Pick<IInterview, 'id'> | null, o2: Pick<IInterview, 'id'> | null): boolean {
    return o1 && o2 ? this.getInterviewIdentifier(o1) === this.getInterviewIdentifier(o2) : o1 === o2;
  }

  addInterviewToCollectionIfMissing<Type extends Pick<IInterview, 'id'>>(
    interviewCollection: Type[],
    ...interviewsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const interviews: Type[] = interviewsToCheck.filter(isPresent);
    if (interviews.length > 0) {
      const interviewCollectionIdentifiers = interviewCollection.map(interviewItem => this.getInterviewIdentifier(interviewItem));
      const interviewsToAdd = interviews.filter(interviewItem => {
        const interviewIdentifier = this.getInterviewIdentifier(interviewItem);
        if (interviewCollectionIdentifiers.includes(interviewIdentifier)) {
          return false;
        }
        interviewCollectionIdentifiers.push(interviewIdentifier);
        return true;
      });
      return [...interviewsToAdd, ...interviewCollection];
    }
    return interviewCollection;
  }

  protected convertDateFromClient<T extends IInterview | NewInterview | PartialUpdateInterview>(interview: T): RestOf<T> {
    return {
      ...interview,
      startedAt: interview.startedAt?.format(DATE_FORMAT) ?? null,
      endedAt: interview.endedAt?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restInterview: RestInterview): IInterview {
    return {
      ...restInterview,
      startedAt: restInterview.startedAt ? dayjs(restInterview.startedAt) : undefined,
      endedAt: restInterview.endedAt ? dayjs(restInterview.endedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestInterview>): HttpResponse<IInterview> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestInterview[]>): HttpResponse<IInterview[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
