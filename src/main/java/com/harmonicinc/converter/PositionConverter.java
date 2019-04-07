package com.harmonicinc.converter;

import com.harmonicinc.imported.model.ImportedPosition;
import com.harmonicinc.model.Position;

public class PositionConverter {
    public ImportedPosition convert(Position position) {
        switch (position) {
            case LEFT:
                return ImportedPosition.LEFT;
            case RIGHT:
                return ImportedPosition.RIGHT;
            default:
                return ImportedPosition.CENTER;
        }
    }
}
