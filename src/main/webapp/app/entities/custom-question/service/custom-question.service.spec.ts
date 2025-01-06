import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICustomQuestion } from '../custom-question.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../custom-question.test-samples';

import { CustomQuestionService } from './custom-question.service';

const requireRestSample: ICustomQuestion = {
  ...sampleWithRequiredData,
};

describe('CustomQuestion Service', () => {
  let service: CustomQuestionService;
  let httpMock: HttpTestingController;
  let expectedResult: ICustomQuestion | ICustomQuestion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CustomQuestionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a CustomQuestion', () => {
      const customQuestion = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(customQuestion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CustomQuestion', () => {
      const customQuestion = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(customQuestion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CustomQuestion', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CustomQuestion', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CustomQuestion', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCustomQuestionToCollectionIfMissing', () => {
      it('should add a CustomQuestion to an empty array', () => {
        const customQuestion: ICustomQuestion = sampleWithRequiredData;
        expectedResult = service.addCustomQuestionToCollectionIfMissing([], customQuestion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(customQuestion);
      });

      it('should not add a CustomQuestion to an array that contains it', () => {
        const customQuestion: ICustomQuestion = sampleWithRequiredData;
        const customQuestionCollection: ICustomQuestion[] = [
          {
            ...customQuestion,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCustomQuestionToCollectionIfMissing(customQuestionCollection, customQuestion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CustomQuestion to an array that doesn't contain it", () => {
        const customQuestion: ICustomQuestion = sampleWithRequiredData;
        const customQuestionCollection: ICustomQuestion[] = [sampleWithPartialData];
        expectedResult = service.addCustomQuestionToCollectionIfMissing(customQuestionCollection, customQuestion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(customQuestion);
      });

      it('should add only unique CustomQuestion to an array', () => {
        const customQuestionArray: ICustomQuestion[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const customQuestionCollection: ICustomQuestion[] = [sampleWithRequiredData];
        expectedResult = service.addCustomQuestionToCollectionIfMissing(customQuestionCollection, ...customQuestionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const customQuestion: ICustomQuestion = sampleWithRequiredData;
        const customQuestion2: ICustomQuestion = sampleWithPartialData;
        expectedResult = service.addCustomQuestionToCollectionIfMissing([], customQuestion, customQuestion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(customQuestion);
        expect(expectedResult).toContain(customQuestion2);
      });

      it('should accept null and undefined values', () => {
        const customQuestion: ICustomQuestion = sampleWithRequiredData;
        expectedResult = service.addCustomQuestionToCollectionIfMissing([], null, customQuestion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(customQuestion);
      });

      it('should return initial array if no CustomQuestion is added', () => {
        const customQuestionCollection: ICustomQuestion[] = [sampleWithRequiredData];
        expectedResult = service.addCustomQuestionToCollectionIfMissing(customQuestionCollection, undefined, null);
        expect(expectedResult).toEqual(customQuestionCollection);
      });
    });

    describe('compareCustomQuestion', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCustomQuestion(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCustomQuestion(entity1, entity2);
        const compareResult2 = service.compareCustomQuestion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCustomQuestion(entity1, entity2);
        const compareResult2 = service.compareCustomQuestion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCustomQuestion(entity1, entity2);
        const compareResult2 = service.compareCustomQuestion(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
