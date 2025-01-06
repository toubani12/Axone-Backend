import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITechCVSoftSkills } from '../tech-cv-soft-skills.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../tech-cv-soft-skills.test-samples';

import { TechCVSoftSkillsService } from './tech-cv-soft-skills.service';

const requireRestSample: ITechCVSoftSkills = {
  ...sampleWithRequiredData,
};

describe('TechCVSoftSkills Service', () => {
  let service: TechCVSoftSkillsService;
  let httpMock: HttpTestingController;
  let expectedResult: ITechCVSoftSkills | ITechCVSoftSkills[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TechCVSoftSkillsService);
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

    it('should create a TechCVSoftSkills', () => {
      const techCVSoftSkills = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(techCVSoftSkills).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TechCVSoftSkills', () => {
      const techCVSoftSkills = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(techCVSoftSkills).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TechCVSoftSkills', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TechCVSoftSkills', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TechCVSoftSkills', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTechCVSoftSkillsToCollectionIfMissing', () => {
      it('should add a TechCVSoftSkills to an empty array', () => {
        const techCVSoftSkills: ITechCVSoftSkills = sampleWithRequiredData;
        expectedResult = service.addTechCVSoftSkillsToCollectionIfMissing([], techCVSoftSkills);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(techCVSoftSkills);
      });

      it('should not add a TechCVSoftSkills to an array that contains it', () => {
        const techCVSoftSkills: ITechCVSoftSkills = sampleWithRequiredData;
        const techCVSoftSkillsCollection: ITechCVSoftSkills[] = [
          {
            ...techCVSoftSkills,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTechCVSoftSkillsToCollectionIfMissing(techCVSoftSkillsCollection, techCVSoftSkills);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TechCVSoftSkills to an array that doesn't contain it", () => {
        const techCVSoftSkills: ITechCVSoftSkills = sampleWithRequiredData;
        const techCVSoftSkillsCollection: ITechCVSoftSkills[] = [sampleWithPartialData];
        expectedResult = service.addTechCVSoftSkillsToCollectionIfMissing(techCVSoftSkillsCollection, techCVSoftSkills);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(techCVSoftSkills);
      });

      it('should add only unique TechCVSoftSkills to an array', () => {
        const techCVSoftSkillsArray: ITechCVSoftSkills[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const techCVSoftSkillsCollection: ITechCVSoftSkills[] = [sampleWithRequiredData];
        expectedResult = service.addTechCVSoftSkillsToCollectionIfMissing(techCVSoftSkillsCollection, ...techCVSoftSkillsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const techCVSoftSkills: ITechCVSoftSkills = sampleWithRequiredData;
        const techCVSoftSkills2: ITechCVSoftSkills = sampleWithPartialData;
        expectedResult = service.addTechCVSoftSkillsToCollectionIfMissing([], techCVSoftSkills, techCVSoftSkills2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(techCVSoftSkills);
        expect(expectedResult).toContain(techCVSoftSkills2);
      });

      it('should accept null and undefined values', () => {
        const techCVSoftSkills: ITechCVSoftSkills = sampleWithRequiredData;
        expectedResult = service.addTechCVSoftSkillsToCollectionIfMissing([], null, techCVSoftSkills, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(techCVSoftSkills);
      });

      it('should return initial array if no TechCVSoftSkills is added', () => {
        const techCVSoftSkillsCollection: ITechCVSoftSkills[] = [sampleWithRequiredData];
        expectedResult = service.addTechCVSoftSkillsToCollectionIfMissing(techCVSoftSkillsCollection, undefined, null);
        expect(expectedResult).toEqual(techCVSoftSkillsCollection);
      });
    });

    describe('compareTechCVSoftSkills', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTechCVSoftSkills(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTechCVSoftSkills(entity1, entity2);
        const compareResult2 = service.compareTechCVSoftSkills(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTechCVSoftSkills(entity1, entity2);
        const compareResult2 = service.compareTechCVSoftSkills(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTechCVSoftSkills(entity1, entity2);
        const compareResult2 = service.compareTechCVSoftSkills(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
