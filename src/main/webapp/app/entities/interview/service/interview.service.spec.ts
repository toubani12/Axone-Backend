import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IInterview } from '../interview.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../interview.test-samples';

import { InterviewService, RestInterview } from './interview.service';

const requireRestSample: RestInterview = {
  ...sampleWithRequiredData,
  startedAt: sampleWithRequiredData.startedAt?.format(DATE_FORMAT),
  endedAt: sampleWithRequiredData.endedAt?.format(DATE_FORMAT),
};

describe('Interview Service', () => {
  let service: InterviewService;
  let httpMock: HttpTestingController;
  let expectedResult: IInterview | IInterview[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InterviewService);
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

    it('should create a Interview', () => {
      const interview = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(interview).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Interview', () => {
      const interview = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(interview).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Interview', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Interview', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Interview', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addInterviewToCollectionIfMissing', () => {
      it('should add a Interview to an empty array', () => {
        const interview: IInterview = sampleWithRequiredData;
        expectedResult = service.addInterviewToCollectionIfMissing([], interview);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(interview);
      });

      it('should not add a Interview to an array that contains it', () => {
        const interview: IInterview = sampleWithRequiredData;
        const interviewCollection: IInterview[] = [
          {
            ...interview,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInterviewToCollectionIfMissing(interviewCollection, interview);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Interview to an array that doesn't contain it", () => {
        const interview: IInterview = sampleWithRequiredData;
        const interviewCollection: IInterview[] = [sampleWithPartialData];
        expectedResult = service.addInterviewToCollectionIfMissing(interviewCollection, interview);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(interview);
      });

      it('should add only unique Interview to an array', () => {
        const interviewArray: IInterview[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const interviewCollection: IInterview[] = [sampleWithRequiredData];
        expectedResult = service.addInterviewToCollectionIfMissing(interviewCollection, ...interviewArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const interview: IInterview = sampleWithRequiredData;
        const interview2: IInterview = sampleWithPartialData;
        expectedResult = service.addInterviewToCollectionIfMissing([], interview, interview2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(interview);
        expect(expectedResult).toContain(interview2);
      });

      it('should accept null and undefined values', () => {
        const interview: IInterview = sampleWithRequiredData;
        expectedResult = service.addInterviewToCollectionIfMissing([], null, interview, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(interview);
      });

      it('should return initial array if no Interview is added', () => {
        const interviewCollection: IInterview[] = [sampleWithRequiredData];
        expectedResult = service.addInterviewToCollectionIfMissing(interviewCollection, undefined, null);
        expect(expectedResult).toEqual(interviewCollection);
      });
    });

    describe('compareInterview', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInterview(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareInterview(entity1, entity2);
        const compareResult2 = service.compareInterview(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareInterview(entity1, entity2);
        const compareResult2 = service.compareInterview(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareInterview(entity1, entity2);
        const compareResult2 = service.compareInterview(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
