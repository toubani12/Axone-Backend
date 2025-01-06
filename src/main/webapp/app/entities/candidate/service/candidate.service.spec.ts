import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICandidate } from '../candidate.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../candidate.test-samples';

import { CandidateService } from './candidate.service';

const requireRestSample: ICandidate = {
  ...sampleWithRequiredData,
};

describe('Candidate Service', () => {
  let service: CandidateService;
  let httpMock: HttpTestingController;
  let expectedResult: ICandidate | ICandidate[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CandidateService);
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

    it('should create a Candidate', () => {
      const candidate = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(candidate).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Candidate', () => {
      const candidate = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(candidate).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Candidate', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Candidate', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Candidate', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCandidateToCollectionIfMissing', () => {
      it('should add a Candidate to an empty array', () => {
        const candidate: ICandidate = sampleWithRequiredData;
        expectedResult = service.addCandidateToCollectionIfMissing([], candidate);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(candidate);
      });

      it('should not add a Candidate to an array that contains it', () => {
        const candidate: ICandidate = sampleWithRequiredData;
        const candidateCollection: ICandidate[] = [
          {
            ...candidate,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCandidateToCollectionIfMissing(candidateCollection, candidate);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Candidate to an array that doesn't contain it", () => {
        const candidate: ICandidate = sampleWithRequiredData;
        const candidateCollection: ICandidate[] = [sampleWithPartialData];
        expectedResult = service.addCandidateToCollectionIfMissing(candidateCollection, candidate);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(candidate);
      });

      it('should add only unique Candidate to an array', () => {
        const candidateArray: ICandidate[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const candidateCollection: ICandidate[] = [sampleWithRequiredData];
        expectedResult = service.addCandidateToCollectionIfMissing(candidateCollection, ...candidateArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const candidate: ICandidate = sampleWithRequiredData;
        const candidate2: ICandidate = sampleWithPartialData;
        expectedResult = service.addCandidateToCollectionIfMissing([], candidate, candidate2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(candidate);
        expect(expectedResult).toContain(candidate2);
      });

      it('should accept null and undefined values', () => {
        const candidate: ICandidate = sampleWithRequiredData;
        expectedResult = service.addCandidateToCollectionIfMissing([], null, candidate, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(candidate);
      });

      it('should return initial array if no Candidate is added', () => {
        const candidateCollection: ICandidate[] = [sampleWithRequiredData];
        expectedResult = service.addCandidateToCollectionIfMissing(candidateCollection, undefined, null);
        expect(expectedResult).toEqual(candidateCollection);
      });
    });

    describe('compareCandidate', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCandidate(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCandidate(entity1, entity2);
        const compareResult2 = service.compareCandidate(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCandidate(entity1, entity2);
        const compareResult2 = service.compareCandidate(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCandidate(entity1, entity2);
        const compareResult2 = service.compareCandidate(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
