import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICustomQuestion, NewCustomQuestion } from '../custom-question.model';

export type PartialUpdateCustomQuestion = Partial<ICustomQuestion> & Pick<ICustomQuestion, 'id'>;

export type EntityResponseType = HttpResponse<ICustomQuestion>;
export type EntityArrayResponseType = HttpResponse<ICustomQuestion[]>;

@Injectable({ providedIn: 'root' })
export class CustomQuestionService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/custom-questions');

  create(customQuestion: NewCustomQuestion): Observable<EntityResponseType> {
    return this.http.post<ICustomQuestion>(this.resourceUrl, customQuestion, { observe: 'response' });
  }

  update(customQuestion: ICustomQuestion): Observable<EntityResponseType> {
    return this.http.put<ICustomQuestion>(`${this.resourceUrl}/${this.getCustomQuestionIdentifier(customQuestion)}`, customQuestion, {
      observe: 'response',
    });
  }

  partialUpdate(customQuestion: PartialUpdateCustomQuestion): Observable<EntityResponseType> {
    return this.http.patch<ICustomQuestion>(`${this.resourceUrl}/${this.getCustomQuestionIdentifier(customQuestion)}`, customQuestion, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICustomQuestion>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICustomQuestion[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCustomQuestionIdentifier(customQuestion: Pick<ICustomQuestion, 'id'>): number {
    return customQuestion.id;
  }

  compareCustomQuestion(o1: Pick<ICustomQuestion, 'id'> | null, o2: Pick<ICustomQuestion, 'id'> | null): boolean {
    return o1 && o2 ? this.getCustomQuestionIdentifier(o1) === this.getCustomQuestionIdentifier(o2) : o1 === o2;
  }

  addCustomQuestionToCollectionIfMissing<Type extends Pick<ICustomQuestion, 'id'>>(
    customQuestionCollection: Type[],
    ...customQuestionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const customQuestions: Type[] = customQuestionsToCheck.filter(isPresent);
    if (customQuestions.length > 0) {
      const customQuestionCollectionIdentifiers = customQuestionCollection.map(customQuestionItem =>
        this.getCustomQuestionIdentifier(customQuestionItem),
      );
      const customQuestionsToAdd = customQuestions.filter(customQuestionItem => {
        const customQuestionIdentifier = this.getCustomQuestionIdentifier(customQuestionItem);
        if (customQuestionCollectionIdentifiers.includes(customQuestionIdentifier)) {
          return false;
        }
        customQuestionCollectionIdentifiers.push(customQuestionIdentifier);
        return true;
      });
      return [...customQuestionsToAdd, ...customQuestionCollection];
    }
    return customQuestionCollection;
  }
}
