import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITechCVDocs } from '../tech-cv-docs.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../tech-cv-docs.test-samples';

import { TechCVDocsService } from './tech-cv-docs.service';

const requireRestSample: ITechCVDocs = {
  ...sampleWithRequiredData,
};

describe('TechCVDocs Service', () => {
  let service: TechCVDocsService;
  let httpMock: HttpTestingController;
  let expectedResult: ITechCVDocs | ITechCVDocs[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TechCVDocsService);
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

    it('should create a TechCVDocs', () => {
      const techCVDocs = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(techCVDocs).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TechCVDocs', () => {
      const techCVDocs = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(techCVDocs).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TechCVDocs', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TechCVDocs', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TechCVDocs', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTechCVDocsToCollectionIfMissing', () => {
      it('should add a TechCVDocs to an empty array', () => {
        const techCVDocs: ITechCVDocs = sampleWithRequiredData;
        expectedResult = service.addTechCVDocsToCollectionIfMissing([], techCVDocs);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(techCVDocs);
      });

      it('should not add a TechCVDocs to an array that contains it', () => {
        const techCVDocs: ITechCVDocs = sampleWithRequiredData;
        const techCVDocsCollection: ITechCVDocs[] = [
          {
            ...techCVDocs,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTechCVDocsToCollectionIfMissing(techCVDocsCollection, techCVDocs);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TechCVDocs to an array that doesn't contain it", () => {
        const techCVDocs: ITechCVDocs = sampleWithRequiredData;
        const techCVDocsCollection: ITechCVDocs[] = [sampleWithPartialData];
        expectedResult = service.addTechCVDocsToCollectionIfMissing(techCVDocsCollection, techCVDocs);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(techCVDocs);
      });

      it('should add only unique TechCVDocs to an array', () => {
        const techCVDocsArray: ITechCVDocs[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const techCVDocsCollection: ITechCVDocs[] = [sampleWithRequiredData];
        expectedResult = service.addTechCVDocsToCollectionIfMissing(techCVDocsCollection, ...techCVDocsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const techCVDocs: ITechCVDocs = sampleWithRequiredData;
        const techCVDocs2: ITechCVDocs = sampleWithPartialData;
        expectedResult = service.addTechCVDocsToCollectionIfMissing([], techCVDocs, techCVDocs2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(techCVDocs);
        expect(expectedResult).toContain(techCVDocs2);
      });

      it('should accept null and undefined values', () => {
        const techCVDocs: ITechCVDocs = sampleWithRequiredData;
        expectedResult = service.addTechCVDocsToCollectionIfMissing([], null, techCVDocs, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(techCVDocs);
      });

      it('should return initial array if no TechCVDocs is added', () => {
        const techCVDocsCollection: ITechCVDocs[] = [sampleWithRequiredData];
        expectedResult = service.addTechCVDocsToCollectionIfMissing(techCVDocsCollection, undefined, null);
        expect(expectedResult).toEqual(techCVDocsCollection);
      });
    });

    describe('compareTechCVDocs', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTechCVDocs(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTechCVDocs(entity1, entity2);
        const compareResult2 = service.compareTechCVDocs(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTechCVDocs(entity1, entity2);
        const compareResult2 = service.compareTechCVDocs(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTechCVDocs(entity1, entity2);
        const compareResult2 = service.compareTechCVDocs(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
