package com.msp.services.impl;

import com.msp.mapper.AncillaryMapper;
import com.msp.mapper.InsuranceCoverageMapper;
import com.msp.models.Ancillary;
import com.msp.models.InsuranceCoverage;
import com.msp.payloads.requests.AncillaryRequest;
import com.msp.payloads.responses.AncillaryResponse;
import com.msp.payloads.responses.InsuranceCoverageResponse;
import com.msp.repositories.AncillaryRepository;
import com.msp.repositories.InsuranceCoverageRepository;
import com.msp.services.AncillaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AncillaryServiceImpl implements AncillaryService {

    private final AncillaryRepository ancillaryRepository;
    private final InsuranceCoverageRepository insuranceCoverageRepository;

    @Override
    public AncillaryResponse createAncillary(Long userId, AncillaryRequest request) {
        Ancillary ancillary = Ancillary.builder()
                .type(request.getType())
                .subType(request.getSubType())
                .rfisc(request.getRfisc())
                .name(request.getName())
                .description(request.getDescription())
                .metadata(request.getMetadata())
                .displayOrder(request.getDisplayOrder())
                .airlineId(request.getAirlineId())
                .build();
        Ancillary saved = ancillaryRepository.save(ancillary);

        return AncillaryMapper.toResponse(saved, null);
    }

    @Override
    public AncillaryResponse getById(Long id) throws Exception {
        Ancillary ancillary = ancillaryRepository.findById(id)
                .orElseThrow(() -> new Exception("Ancillary with given id not found!"));

        //fetch insurance coverages by ancillary
        List<InsuranceCoverage> coverages = insuranceCoverageRepository
                .findByAncillaryId(ancillary.getId());
        List<InsuranceCoverageResponse> coveragesResponseList = coverages.stream()
                .map(InsuranceCoverageMapper::toResponse)
                .toList();

        return AncillaryMapper.toResponse(ancillary, coveragesResponseList);
    }

    @Override
    public List<AncillaryResponse> getByAirlineId(Long userId) {
        return  ancillaryRepository.findByAirlineId(userId).stream()
                .map(anc -> {
                    //fetch insurance coverages by ancillary
                    List<InsuranceCoverage> coverages = insuranceCoverageRepository
                            .findByAncillaryId(anc.getId());

                    List<InsuranceCoverageResponse> coveragesResponseList = coverages.stream()
                            .map(InsuranceCoverageMapper::toResponse)
                            .toList();

                    return AncillaryMapper
                                    .toResponse(anc, coveragesResponseList);
                }).toList();
    }

    @Override
    public AncillaryResponse updateAncillary(Long id, AncillaryRequest request) throws Exception {
        Ancillary existing = ancillaryRepository.findById(id)
                .orElseThrow(() -> new Exception("Ancillary with given id not found!"));

        if(request.getType()!=null)existing.setType(request.getType());
        if(request.getSubType()!=null) existing.setSubType(request.getSubType());
        if(request.getRfisc()!=null) existing.setRfisc(request.getRfisc());
        if(request.getName()!=null) existing.setName(request.getName());
        if(request.getDescription()!=null) existing.setDescription(request.getDescription());
        if(request.getMetadata()!=null) existing.setMetadata(request.getMetadata());
        if(request.getDisplayOrder()!=null) existing.setDisplayOrder(request.getDisplayOrder());

        Ancillary updated = ancillaryRepository.save(existing);

        //fetch insurance coverages by ancillary
        List<InsuranceCoverage> coverages = insuranceCoverageRepository
                .findByAncillaryId(updated.getId());
        List<InsuranceCoverageResponse> coveragesResponseList = coverages.stream()
                .map(InsuranceCoverageMapper::toResponse)
                .toList();

        return AncillaryMapper.toResponse(existing, coveragesResponseList);
    }

    @Override
    public void deleteAncillary(Long id) throws Exception {
        Ancillary ancillary = ancillaryRepository.findById(id)
                .orElseThrow(() -> new Exception("Ancillary with given id not found!"));
        ancillaryRepository.delete(ancillary);
    }
}
